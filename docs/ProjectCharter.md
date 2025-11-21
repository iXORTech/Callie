# Callie

This document describes the basic overview and concepts of the Callie project, including its purpose, scope, and high-level architecture.

> [!WARNING]
> Some of the design of this software are **NOT** designed with full security in mind, so this software should **NEVER** be used in production and real-life cases.

## Project Overview

Callie is a relatively basic chat app created as a toy project for learning purposes. It is not intended to be a production-ready application, but rather a platform for exploring various technologies and concepts in web development, real-time communication, database management, and more.

This project is **never meant to be used in production** and is not designed with security, scalability, or performance in mind. It is a learning tool and should be treated as such.

The project has two (type of) components, server-side application(s) and client-side application(s). There is only one official server-side application planned and developed, but multiple client-side applications can be created to connect to the server as long as they comply with the communication protocols. Two client-side applications are currently being planned and developed, a CLI-then-TUI based one and a GUI based one. Each of them has a different tech stack, except that standard network protocols (HTTP, WebSocket) are used on both sides to implement communication features between them.
