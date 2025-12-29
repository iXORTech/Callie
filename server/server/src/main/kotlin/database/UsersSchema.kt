package dev.ixor.callie.database

import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class ExposedUser(val username: String)

class UserService(database: Database) {
    object Users : Table() {
        val id = integer("id").autoIncrement()
        val username = varchar("username", length = 50).uniqueIndex()
        val passwordHash = varchar("passwordHash", length = 255)
        val createdAt = long("created_at").default(System.currentTimeMillis())
        val updatedAt = long("updated_at").default(System.currentTimeMillis())

        override val primaryKey = PrimaryKey(id)
    }

    object ClientTokens : Table() {
        val id = integer("id")
        val userId = integer("user_id").references(Users.id)
        val machineId = varchar("machine_id", length = 64).uniqueIndex()
        val tokenHash = varchar("token_hash", length = 255)
        val createdAt = long("created_at").default(System.currentTimeMillis())

        override val primaryKey = PrimaryKey(id)
    }

    object SessionTokens : Table("session_tokens") {
        val id = integer("id")
        val userId = integer("user_id").references(Users.id)
        val createdAt = long("created_at").default(System.currentTimeMillis())
        val expiresAt = long("expires_at").nullable()

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Users)
        }
    }

    suspend fun create(user: ExposedUser): Int = dbQuery {
        Users.insert {
            it[username] = user.username
        }[Users.id]
    }

    suspend fun read(id: Int): ExposedUser? {
        return dbQuery {
            Users.selectAll()
                .where { Users.id eq id }
                .map { ExposedUser(it[Users.username]) }
                .singleOrNull()
        }
    }

    suspend fun update(id: Int, user: ExposedUser) {
        dbQuery {
            Users.update({ Users.id eq id }) {
                it[username] = user.username
            }
        }
    }

    suspend fun delete(id: Int) {
        dbQuery {
            Users.deleteWhere { Users.id.eq(id) }
        }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
