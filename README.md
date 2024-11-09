# Basic Java HTTP Server

A simple, single-threaded HTTP server implementation in Java that demonstrates basic web server functionality. This server is designed for educational purposes to understand the fundamentals of HTTP communication.

## Features

- **Basic HTTP Request Handling**: Serves static files over HTTP
- **Support for Multiple File Types**:
  - HTML files (`.html`)
  - Favicon icon (`.ico`)
  - PNG images (`.png`)
- **404 Error Handling**: Custom error page for non-existent resources
- **Content Type Detection**: Basic MIME type support for different file formats

## Technical Specifications

- **Port**: Runs on port 8080
- **Protocol**: HTTP/1.0
- **Connection Type**: Single-threaded (handles one request at a time)
- **File Location**: Serves files from `src/io/codeforall/fanstatics/` directory

## Project Structure

```
src/
└── io/
    └── codeforall/
        └── fanstatics/
            ├── HTTPServer.java
            ├── index.html
            ├── favicon.ico
            ├── logo.png
            └── 404.html
```

## Supported File Types

| File Type | Content-Type Header |
|-----------|-------------------|
| HTML | `text/html; charset=UTF-8` |
| ICO | `image/ico` |
| PNG | `image/png` |

## Usage

1. Compile the Java code:
```bash
javac src/io/codeforall/fanstatics/HTTPServer.java
```

2. Run the server:
```bash
java io.codeforall.fanstatics.HTTPServer
```

3. Access the server through a web browser:
```
http://localhost:8080/index.html
```

## Limitations

- Handles only one connection at a time
- No support for POST requests
- Limited error handling
- No support for directory listings
- Server stops after serving one request
- Basic security implementation

## Future Improvements

1. Add multi-threading support for multiple concurrent connections
2. Implement continuous server operation
3. Add support for additional HTTP methods (POST, PUT, DELETE)
4. Improve error handling and logging
5. Add configuration options (port, root directory)
6. Implement proper file streaming for large files
7. Add security features
8. Support for additional MIME types

## Educational Value

This project serves as a practical example for learning:
- Basic HTTP protocol implementation
- Socket programming in Java
- File I/O operations
- HTTP header formatting
- MIME type handling
- Web server fundamentals

## Note

This is a basic implementation intended for educational purposes and should not be used in production environments. For production use, consider using established web servers or frameworks.
