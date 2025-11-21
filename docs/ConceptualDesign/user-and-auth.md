# User and Authentication in Callie

> [!NOTE]
> Note that this sections **is not** for authentication of the web management/monitoring interface, but for the user who will be using the client application, connected to the server, and sending messages to other users.

In the Callie system, a user has the following properties, stores across the database and memory:

- **Username**: A unique identifier for the user.
- **Password**: An [Argon2 hashed](https://github.com/bcgit/bc-java) password for authentication.
- A list of **Client Tokens**: Each of these tokens is associated with a client, and is used to validate authenticated clients so users doesn't have to re-login. This token doesn't expire unless the user choose to invalidate it.
- A list of **Session Tokens**: Each of these tokens is associated with a live (online) client session, and is used to identify connected WebSocket sessions. This token expires *every time the WebSocket connection is closed*.

The client authentication feature is implemented using a simple username/password-token system. To connect to the WebSocket handling the chat messages, the client must provide a one-time session token, and the WebSocket interface will always expect a token to be sent by the client as the first message (in a special format specified below), before the WebSocket begin to attempt to read from or write to the connection. If the token is not provided in a timeout period (currently 1 minute), the WebSocket connection will be closed by the server.

The session token generation and user login process follows the steps described below:

1. The client application sends an  `application/json` POST request to the `/api/v1/auth/login` endpoint.

    This endpoint **always** takes a [Machine ID](https://crates.io/crates/mid) (or any other ID that identifies a machine uniquely) and a `authType` string indicating what other info are needed for authentication, which should be either `"PASSWD"` or `"TOKEN"` (case-sensitive).

    When there is no stored client token, the `authType` is `"PASSWD"`, and the client sends a `username` and a `password` (in plaintext) with the above mentioned information.

    When there is a stored client token, the `authType` is `TOKEN`, and the client will provide a `username` and a `clientToken` (the stored client token in plaintext) with the above mentioned information.

2. The server checks the credentials (password and token) against the database.

    If the `authType` is `PASSWD`, the server just checks the username-password pair store in the database. The authentication is successful if they match, and is failed otherwise.

    If the `authType` is `TOKEN`, the server first check if the client token is matched with the provided username, and then a new client token is generated again from the exact same information (username, hashed password, and Machine ID) and compared with the provided client token. The authentication is successful if they match, and is failed otherwise.

3. If the credentials are valid, the server generates a session token and returns it in the response (with a client token, if the user was password authenticated, the client token should be generated from some combination of username, hashed password and Machine ID, or, if the user was client token authenticated, the client token is returned as-is). Otherwise, the server *should* always go back to step no.1 and require a password authentication from that MachineID/IP.
4. The client application stores the session token and uses it to authenticate subsequent WebSocket connections.
