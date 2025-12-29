package dev.ixor.callie.routes.security

import org.bouncycastle.crypto.generators.Argon2BytesGenerator
import org.bouncycastle.crypto.params.Argon2Parameters
import org.bouncycastle.util.encoders.Hex
import java.security.SecureRandom

/**
 * Password hashing service using Bouncy Castle's Argon2 implementation.
 *
 * based on github.com/bcgit/bc-java
 */
object PasswordService {
    private const val ARGON2_TYPE = Argon2Parameters.ARGON2_id
    private const val ARGON2_VERSION = Argon2Parameters.ARGON2_VERSION_13

    // OWASP recommended parameters
    private const val ITERATIONS = 3
    private const val MEMORY = 65536    // 64 MB
    private const val PARALLELISM = 4
    private const val HASH_LENGTH = 32
    private const val SALT_LENGTH = 16

    private val secureRandom = SecureRandom()

    /**
     * Hashes a password using Argon2id.
     *
     * @param password The plaintext password to hash.
     * @return The hashed password in the format: $argon2id$v=19$m=65536,t=3,p=4$<salt>$<hash>
     */
    fun hash(password: String): String {
        val salt = ByteArray(SALT_LENGTH)
        secureRandom.nextBytes(salt)

        val hash = generateHash(password.toCharArray(), salt)

        // Encode for storage
        // Format: $argon2id$v=19$m=65536,t=3,p=4$<base64_salt>$<base64_hash>
        val saltHex = Hex.toHexString(salt)
        val hashHex = Hex.toHexString(hash)

        return "\$argon2id\$v=$ARGON2_VERSION\$m=$MEMORY,t=$ITERATIONS,p=$PARALLELISM\$$saltHex\$$hashHex"
    }

    /**
     * Generates the Argon2 hash for the given password and salt.
     */
    private fun generateHash(
        password: CharArray,
        salt: ByteArray,
        memory: Int = MEMORY,
        iterations: Int = ITERATIONS,
        parallelism: Int = PARALLELISM
    ): ByteArray {
        val builder = Argon2Parameters.Builder(ARGON2_TYPE)
            .withVersion(ARGON2_VERSION)
            .withIterations(iterations)
            .withMemoryAsKB(memory)
            .withParallelism(parallelism)
            .withSalt(salt)

        val generator = Argon2BytesGenerator()
        generator.init(builder.build())

        val result = ByteArray(HASH_LENGTH)
        generator.generateBytes(password, result, 0, result.size)

        return result
    }

    /**
     * Verify a password against a stored Argon2id hash.
     *
     * @param password The plaintext password to verify.
     * @param storedHash The stored hash to verify against.
     * @return True if the password matches the hash, false otherwise.
     */
    fun verify(password:  String, storedHash: String): Boolean {
        return try {
            val parts = parseHashString(storedHash)
            val salt = Hex.decode(parts. salt)
            val expectedHash = Hex.decode(parts.hash)

            val computedHash = generateHash(
                password.toCharArray(),
                salt,
                parts.memory,
                parts. iterations,
                parts.parallelism
            )

            constantTimeEquals(expectedHash, computedHash)
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Parses the stored hash string into its parameters.
     */
    private fun parseHashString(hashString: String): HashComponents {
        // Expected format: $argon2id$v=19$m=65536,t=3,p=4$<salt>$<hash>
        val parts = hashString. split("$").filter { it.isNotEmpty() }

        require(parts.size == 5) { "Invalid hash format" }
        require(parts[0] == "argon2id") { "Only argon2id is supported" }

        val version = parts[1]. removePrefix("v=").toInt()
        val params = parts[2]. split(",").associate {
            val (key, value) = it.split("=")
            key to value. toInt()
        }

        return HashComponents(
            version = version,
            memory = params["m"] ?:  MEMORY,
            iterations = params["t"] ?:  ITERATIONS,
            parallelism = params["p"] ?:  PARALLELISM,
            salt = parts[3],
            hash = parts[4]
        )
    }

    /**
     * Compares two byte arrays in constant time to prevent timing attacks.
     */
    private fun constantTimeEquals(a: ByteArray, b:  ByteArray): Boolean {
        if (a.size != b.size) return false

        var result = 0
        for (i in a.indices) {
            result = result or (a[i]. toInt() xor b[i]. toInt())
        }
        return result == 0
    }

    /**
     * Data class to hold parsed hash components.
     */
    private data class HashComponents(
        val version:  Int,
        val memory: Int,
        val iterations: Int,
        val parallelism: Int,
        val salt:  String,
        val hash: String
    )
}
