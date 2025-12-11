# Real-Time Messaging in Callie

The core feature of Callie is real-time messaging between users. This is implemented using WebSockets, allowing a low-latency, full-duplex communication channel between the client and server that serves as the backbone for chat functionalities.

When a client connects to the WebSocket endpoint, it must first authenticate using the session token obtained during the login process, as described in the [User and Authentication](user-and-auth.md) document. Once authenticated, the client can send and receive messages in real-time.

In the Callie system, messages that are transmitted between clients and/or server must be serialized into a specific JSON format before being sent over the WebSocket connection. A specific payload transmitted over the WebSocket with the following structure:

```json
{
    "token": "SESSION_TOKEN",
    "sender": "SENDER_IDENTIFIER",
    "recipient": "RECIPIENT_IDENTIFIER",
    "timestamp": "ISO_8601_TIMESTAMP",
    "type": "PAYLOAD_TYPE",
    "content": {
        // Specific content of the payload based on PAYLOAD_TYPE.
    },
    "metadata": {
        // Optional additional metadata, e.g. message ID, extra flags, etc.
    },
    "payloadID": "UNIQUE_PAYLOAD_IDENTIFIER",
    "payloadVersion": "1.0"
}
```

where:

- `token`: The session token that the client obtained during the login process. This is used to authenticate the WebSocket connection.
- `sender`: The identifier of the sender (usually the username).
- `recipient`: The identifier of the recipient (usually the username, group ID, or special keywords like `SERVER` or `BROADCAST`).
- `timestamp`: The timestamp when the message is sent, in ISO 8601 format, precisely to milliseconds (`YYYY-MM-DDTHH:mm.ss.sssZ`), in UTC timezone.
- `type`: The type of the payload indicating the nature of the content, more details in the following sections.
- `content`: The actual content of the message, structured according to the `type` field, detailed in the following sections.
- `metadata`: Optional additional metadata that may include message IDs, flags, or other relevant information.
- `payloadID`: A unique identifier for the payload, used for tracking and referencing specific messages.
- `payloadVersion`: The version of the payload format, currently set to `1.0`.

## Payload Types and Content Structure

The `type` field in the payload indicates the nature of the content being transmitted. The following are the defined payload types and their corresponding content structures.

### PING and PONG

- **Type**: `PING` or `PONG`
- **Content Structure**:

    ```json
    "content": {
    }
    ```

- **Metadata**:

    ```json
    "metadata": {
    }
    ```

- **Description**: Used for keep-alive messages to ensure the connection is active or to check if the other party is responsive. A client or the server can send a `PING` payload to which the recipient must respond with a `PONG` payload to confirm connectivity.

### Chat Message

- **Type**: `MSG`
- **Content Structure**:

    ```json
    "content": {
        "messageID": "UNIQUE_MESSAGE_IDENTIFIER",
        "text": "MESSAGE_TEXT_CONTENT",
        "attachments": [
            {
                "type": "ATTACHMENT_TYPE",
                "url": "ATTACHMENT_URL",
                "metadata": {
                    // Additional metadata about the attachment.
                    "filename": "example.png",
                    "size": 204800,
                    "filetype": "image/png"
                }
            }
        ]
    }
    ```

    - `messageID`: A unique identifier for the message.
    - `text`: The textual content of the message. *Markdown IS supported*, including code blocks, links, images, etc. It is up to the client to render the markdown depending on its capabilities and implementation.
    - `attachments`: An optional array of attachments associated with the message. Each attachment includes:
        - `type`: The type of attachment (`image`, `video`, or `file`), indicating an image file, a video file, or a generic file, respectively.
        - `url`: A URL where the attachment can be accessed or downloaded.
        - `metadata`: Additional metadata about the attachment, such as filename, size (in bytes), and filetype (MIME type).
        - The capabilities of handling attachment uploading, downloading, and rendering depend on the client implementation.
        - NO attachment uploading/handling endpoint is provided by the server in the current design; attachments must be hosted externally.

- **Metadata**:

    ```json
    "metadata": {
    }
    ```

- **Description**: Represents a standard chat message sent from one user to another (or to a group). The message can include text content (with Markdown support) and optional attachments.

### System Message

- **Type**: `SYS`
- **Content Structure**:

    ```json
    "content": {
        "code": "SYSTEM_MESSAGE_CODE",
        "description": "DETAILED_DESCRIPTION_OF_THE_SYSTEM_MESSAGE"
    }
    ```

    - `code`: A predefined code representing the type of system message (e.g., `USER_JOINED`, `USER_LEFT`, `ERROR123`, etc.).
    - `description`: A detailed description of the system message, providing context or information about the event.

- **Metadata**:

    ```json
    "metadata": {
    }
    ```

- **Description**: Used for system-level notifications or events, such as user join/leave notifications, error messages (e.g., invalid message format, authentication failures), or other system-related information. Usually sent by the server to clients.

### Command Message

- **Type**: `CMD`
- **Content Structure**:
    ```json
    "content": {
        "command": "COMMAND_NAME",
        "parameters": {
            // Command-specific parameters.
        }
    }
    ```

    - `command`: The name of the command to be executed (e.g., `CREATE_GROUP`, `DELETE_MESSAGE`, etc.).
    - `parameters`: A JSON object containing command-specific parameters required for executing the command.

- **Metadata**:

    ```json
    "metadata": {
    }
    ```

- **Description**: Used for sending commands from the client to the server or vice versa. Commands can be used to perform various actions, such as creating groups, deleting messages, changing user settings, or other administrative tasks.

## Payload Validation

All incoming payloads must be validated by the server BEFORE they are processed and sent to the intended recipient(s) to ensure they conform to the expected structure and content based on the `type` field. If a payload fails validation, the server should respond with an appropriate system message indicating the error.

## Offline Message Storage

In the current design of Callie, offline message storage is not implemented. This means that if a recipient is offline when a message is sent, the message will not be stored for later delivery. Instead, the message will simply be discarded, and the sender will receive notification indicating a failure to deliver the message.

This design choice simplifies the implementation but may not be suitable for all use cases. Future versions of Callie may consider adding offline message storage capabilities that allows the delivery of messages to users when they come back online.
