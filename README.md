# Chat Application

A simple chat system built in Java using sockets and multithreading. The application allows many clients to connect to a server and displays all messages to their client.

## Features
- Multi-user chat support
- Command-line interface
- Change username with `/user <newName>`
- Disconnect with `/quit`

## How it works

1. **Server** runs on port `1234`, accepts incoming client connections, and spawns a new thread for each client.
2. **Client** connects to the server, prompts the user for a username, and allows sending/receiving messages via the console.
3. All messages are broadcast to every connected client.
4. Special commands:
   - `/user <newName>` to change username
   - `/quit` to disconnect from the server
5. Server maintains a list of connected clients to manage broadcasts and communication.

## 🎯 Potential Future Improvements

- [ ] Add a Swing-based graphical user interface
- [ ] Implement private messaging between users
- [ ] Check for duplicate usernames to avoid conflicts
- [ ] Save chat history to a file for persistence
- [ ] Add admin/moderator commands (e.g., kick, mute)
- [ ] Display list of currently connected users
- [ ] Encrypt messages for secure communication



