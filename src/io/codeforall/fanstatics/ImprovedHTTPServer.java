package io.codeforall.fanstatics;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Enhanced HTTP Server implementation with improved security, resource handling,
 * and support for multiple requests and binary files.
 */
public class ImprovedHTTPServer {
    private static final int PORT = 8080;
    private static final String WEB_ROOT = "src/io/codeforall/fanstatics";
    private static final Map<String, String> MIME_TYPES = new HashMap<>();

    // Initialize MIME type mappings
    static {
        MIME_TYPES.put("html", "text/html; charset=UTF-8");
        MIME_TYPES.put("ico", "image/x-icon");
        MIME_TYPES.put("png", "image/png");
        MIME_TYPES.put("jpg", "image/jpeg");
        MIME_TYPES.put("css", "text/css");
        MIME_TYPES.put("js", "application/javascript");
    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT + "...");

            // Main server loop
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    handleRequest(clientSocket);
                } catch (IOException e) {
                    System.err.println("Error handling request: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + PORT);
            System.exit(1);
        }
    }

    /**
     * Handles a single HTTP request
     * @param clientSocket The socket connected to the client
     */
    private static void handleRequest(Socket clientSocket) {
        try (clientSocket;
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream out = clientSocket.getOutputStream()) {

            // Parse HTTP request
            String requestLine = in.readLine();
            if (requestLine == null) {
                return;
            }

            // Extract request method and path
            String[] requestParts = requestLine.split(" ");
            if (requestParts.length != 3) {
                sendError(out, 400, "Bad Request");
                return;
            }

            String method = requestParts[0];
            String requestPath = requestParts[1];

            // Only handle GET requests
            if (!"GET".equals(method)) {
                sendError(out, 405, "Method Not Allowed");
                return;
            }

            // Process request
            processRequest(requestPath, out);

        } catch (IOException e) {
            System.err.println("Error processing request: " + e.getMessage());
        }
    }

    /**
     * Processes the HTTP request and sends appropriate response
     * @param requestPath The requested resource path
     * @param out The output stream to send the response
     */
    private static void processRequest(String requestPath, OutputStream out) throws IOException {
        // Normalize path and prevent directory traversal
        String normalizedPath = normalizePath(requestPath);
        if (normalizedPath == null) {
            sendError(out, 403, "Forbidden");
            return;
        }

        // Construct full file path
        Path filePath = Paths.get(WEB_ROOT, normalizedPath);

        // Check if file exists and is readable
        if (!Files.exists(filePath) || !Files.isRegularFile(filePath) || !Files.isReadable(filePath)) {
            serveFile(out, Paths.get(WEB_ROOT, "404.html"), "404 Not Found");
            return;
        }

        // Serve the file
        serveFile(out, filePath, "200 OK");
    }

    /**
     * Serves a file with appropriate headers
     * @param out The output stream to send the response
     * @param filePath Path to the file to serve
     * @param status HTTP status line
     */
    private static void serveFile(OutputStream out, Path filePath, String status) throws IOException {
        // Determine content type
        String contentType = getContentType(filePath);
        byte[] content = Files.readAllBytes(filePath);

        // Construct and send headers
        String headers = String.format(
                "HTTP/1.1 %s\r\n" +
                        "Content-Type: %s\r\n" +
                        "Content-Length: %d\r\n" +
                        "Connection: close\r\n" +
                        "\r\n",
                status, contentType, content.length
        );

        // Send response
        out.write(headers.getBytes());
        out.write(content);
        out.flush();
    }

    /**
     * Sends an error response
     * @param out The output stream to send the response
     * @param code HTTP status code
     * @param message Error message
     */
    private static void sendError(OutputStream out, int code, String message) throws IOException {
        String response = String.format(
                "HTTP/1.1 %d %s\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: %d\r\n" +
                        "Connection: close\r\n" +
                        "\r\n" +
                        "<html><body><h1>%d - %s</h1></body></html>",
                code, message, message.length() + 48, code, message
        );
        out.write(response.getBytes());
        out.flush();
    }

    /**
     * Normalizes the request path and prevents directory traversal
     * @param path The request path to normalize
     * @return Normalized path or null if path is invalid
     */
    private static String normalizePath(String path) {
        // Remove query string if present
        int queryStart = path.indexOf('?');
        if (queryStart != -1) {
            path = path.substring(0, queryStart);
        }

        // Convert URL encoding
        path = path.replace("%20", " ");

        // Default to index.html
        if (path.equals("/")) {
            return "index.html";
        }

        // Remove leading slash
        path = path.startsWith("/") ? path.substring(1) : path;

        // Prevent directory traversal
        if (path.contains("..") || path.startsWith("/") || path.startsWith("\\")) {
            return null;
        }

        return path;
    }

    /**
     * Determines MIME type based on file extension
     * @param filePath Path to the file
     * @return Appropriate MIME type
     */
    private static String getContentType(Path filePath) {
        String fileName = filePath.getFileName().toString();
        int extensionIndex = fileName.lastIndexOf('.');
        if (extensionIndex > 0) {
            String extension = fileName.substring(extensionIndex + 1).toLowerCase();
            return MIME_TYPES.getOrDefault(extension, "application/octet-stream");
        }
        return "application/octet-stream";
    }
}