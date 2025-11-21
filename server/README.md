# Callie Server

**JDK Requirement:** `21`.

This project was created using the [Ktor Project Generator](https://start.ktor.io).

Here are some useful links to get you started:

- [Ktor Documentation](https://ktor.io/docs/home.html)
- [Ktor GitHub page](https://github.com/ktorio/ktor)
- The [Ktor Slack chat](https://app.slack.com/client/T09229ZC6/C0A974TJ9). You'll need
  to [request an invite](https://surveys.jetbrains.com/s3/kotlin-slack-sign-up) to join.

## Features

Here's a list of features included in this project:

| Name                                                                   | Description                                                                        |
|------------------------------------------------------------------------|------------------------------------------------------------------------------------|
| [Authentication](https://start.ktor.io/p/auth)                         | Provides extension point for handling the Authorization header                     |
| [Authentication Basic](https://start.ktor.io/p/auth-basic)             | Handles 'Basic' username / password authentication scheme                          |
| [Authentication OAuth](https://start.ktor.io/p/auth-oauth)             | Handles OAuth Bearer authentication scheme                                         |
| [Sessions](https://start.ktor.io/p/ktor-sessions)                      | Adds support for persistent sessions through cookies or headers                    |
| [Request Validation](https://start.ktor.io/p/request-validation)       | Adds validation for incoming requests                                              |
| [Routing](https://start.ktor.io/p/routing)                             | Provides a structured routing DSL                                                  |
| [Server-Sent Events (SSE)](https://start.ktor.io/p/sse)                | Support for server push events                                                     |
| [Static Content](https://start.ktor.io/p/static-content)               | Serves static files from defined locations                                         |
| [Webjars](https://start.ktor.io/p/webjars)                             | Bundles static assets into your built JAR file                                     |
| [Cohort](https://start.ktor.io/p/cohort)                               | Spring Actuator style implementation for Ktor                                      |
| [Content Negotiation](https://start.ktor.io/p/content-negotiation)     | Provides automatic content conversion according to Content-Type and Accept headers |
| [kotlinx.serialization](https://start.ktor.io/p/kotlinx-serialization) | Handles JSON serialization using kotlinx.serialization library                     |
| [CSS DSL](https://start.ktor.io/p/css-dsl)                             | Generates CSS from Kotlin DSL                                                      |
| [Freemarker](https://start.ktor.io/p/freemarker)                       | Serves HTML content using Apache's FreeMarker template engine                      |
| [HTML DSL](https://start.ktor.io/p/html-dsl)                           | Generates HTML from Kotlin DSL                                                     |
| [HTMX](https://start.ktor.io/p/htmx)                                   | Includes HTMX for front-end scripting                                              |
| [Exposed](https://start.ktor.io/p/exposed)                             | Adds Exposed database to your application                                          |
| [WebSockets](https://start.ktor.io/p/ktor-websockets)                  | Adds WebSocket protocol support for bidirectional client connections               |

## Structure

This project includes the following modules:

| Path             | Description                              |
|------------------|------------------------------------------|
| [server](server) | A runnable Ktor server implementation    |
| [web](web)       | Front-end Kotlin scripts for the browser |

## Building

To build the project, use one of the following tasks:

| Task                                            | Description                                                            |
|-------------------------------------------------|------------------------------------------------------------------------|
| `./gradlew build`                               | Build everything                                                       |
| `./gradlew :server:buildFatJar`                 | Build an executable JAR of the server with all dependencies included   |
| `./gradlew :server:buildImage`                  | Build the docker image to use with the fat JAR                         |
| `./gradlew :server:publishImageToLocalRegistry` | Publish the docker image locally                                       |
| `./gradlew :server:build`                       | Build Ktor server                                                      |
| `./gradlew :web:build`                          | Build WASM scripts                                                     |
| `./gradlew -t :server:build`                    | Build Ktor server continuously                                         |
| `./gradlew -t :web:build`                       | Build WASM scripts continuously                                        |

> [!NOTE]
> 
> The `-t :server:build` and `-t :web:build` tasks will automatically rebuild the project when changes are detected.
> 
> The corresponding IntelliJ IDEA task will run until it is stopped manually, allowing the Ktor server to be auto reloaded upon changes.

> [!IMPORTANT]
> 
> If npm dependencies for the front-end are changed, you must run the `./gradlew :kotlinUpgradeYarnLock` task to update the `yarn.lock` file before building the project.

## Running

> [!IMPORTANT]
> 
> Run the `./gradlew :web:build` (or `./gradlew -t :web:build`) task before running the server to ensure that the front-end scripts are built and available for the server to serve.

> [!IMPORTANT]
> 
> To let Ktor auto reload the server when changes are made, you must run the `./gradlew -t :server:build` task before running the server.
> 
> This will ensure that the server is rebuilt and reloaded automatically when changes are detected in the server code and let the changes reflect in the running server without needing to restart it manually.
> 
> THIS IS ONLY FOR DEVELOPMENT PURPOSES.
> 
> **Pages with HTMX will not function properly after auto reload unless the server is manually restarted.**

To run the project, use one of the following tasks:

| Task                                 | Description                            |
|--------------------------------------|----------------------------------------|
| `./gradlew :server:run`              | Run the server                         |
| `./gradlew :server:runDocker`        | Run using the local docker image       |
| `./gradlew -t :web:wasmJsBrowserRun` | Run scripts in a browser, without Ktor |

If the server starts successfully, you'll see the following output:

```
2024-12-04 14:32:45.584 [main] INFO  Application - Application started in 0.303 seconds.
2024-12-04 14:32:45.682 [main] INFO  Application - Responding at http://0.0.0.0:8080
```
