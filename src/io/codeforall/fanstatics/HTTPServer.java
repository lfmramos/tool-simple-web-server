package io.codeforall.fanstatics;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A simple HTTP Server implementation that handles basic web requests
 * Supports serving HTML pages, images, and error pages
 */
public class HTTPServer {

    /**
     * Main method to start the HTTP server and handle incoming requests
     * Currently supports single-threaded operation (one request at a time)
     * @param args Command line arguments (not used)
     * @throws IOException If server encounters network or file I/O errors
     */
    public static void main(String[] args) throws IOException {

        int portNumber = 8080; // Standard HTTP alternative port

        // Initialize server infrastructure
        ServerSocket serverSocket = new ServerSocket(portNumber);
        System.out.println("Server is listening on port " + portNumber + "...");
        System.out.println("Waiting for connection...");
        // Block until client connects
        Socket clientSocket = serverSocket.accept();
        System.out.println("Connection accepted from: " + clientSocket.getRemoteSocketAddress());

        // Set up communication channels
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String browserMessage = in.readLine(); // Read HTTP request line

        // Set up response channels
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        OutputStream outputStream = clientSocket.getOutputStream();

        // Route requests based on requested resource
        if (browserMessage.contains("index.html")) {

            StringBuilder fileContent = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader("src/io/codeforall/fanstatics/index.html"));

            // Transform content to String
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
            String fileStr = new String(fileContent);
            byte[] file = fileStr.getBytes();

            //header
            String headerStr = new String("HTTP/1.0 200 Document Follows\r\n" +
                    "Content-Type: text/html; charset=UTF-8\r\n" +
                    "Content-Length: " + file.length + "\r\n" +
                    "\r\n");

            System.out.println(headerStr);
            System.out.println(file);

            out.println(headerStr);
            outputStream.write(file, 0, file.length);

        } else if (browserMessage.contains("favicon.ico")) {
            System.out.println("favicon");

            StringBuilder fileContent = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader("src/io/codeforall/fanstatics/favicon.ico"));

            // Transform content to String
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
            String fileStr = new String(fileContent);
            byte[] file = fileStr.getBytes();

            //header
            String headerStr = new String("HTTP/1.0 200 Document Follows\r\n" +
                    "Content-Type: image/ico \r\n" +
                    "Content-Length: " + file.length + "\r\n" +
                    "\r\n");

            System.out.println(headerStr);
            System.out.println(file);

            out.println(headerStr);
            outputStream.write(file, 0, file.length);

        } else if (browserMessage.contains("logo.png")) {
            System.out.println("logo");
            StringBuilder fileContent = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader("src/io/codeforall/fanstatics/logo.png"));

            // Transform content to String
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
            String fileStr = new String(fileContent);
            byte[] file = fileStr.getBytes();

            //header
            String headerStr = new String("HTTP/1.0 200 Document Follows\r\n" +
                    "Content-Type: image/png \r\n" +
                    "Content-Length: " + file.length + "\r\n" +
                    "\r\n");

            System.out.println(headerStr);
            System.out.println(file);

            out.println(headerStr);
            outputStream.write(file, 0, file.length);

        } else {
            System.out.println("404");
            StringBuilder fileContent = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader("src/io/codeforall/fanstatics/404.html"));

            // Read file content
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
            // Prepare response
            String fileStr = new String(fileContent);
            byte[] file = fileStr.getBytes();

            // Construct HTTP header
            String headerStr = new String("HTTP/1.0 404 Not Found\r\n" +
                    "Content-Type: text/html; charset=UTF-8\r\n" +
                    "Content-Length: " + file.length + "\r\n" +
                    "\r\n");

            // Log response
            System.out.println(headerStr);
            System.out.println(file);

            // Send response
            out.println(headerStr);
            outputStream.write(file, 0, file.length);
        }
    }
}