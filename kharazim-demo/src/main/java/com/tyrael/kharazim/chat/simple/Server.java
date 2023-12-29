package com.tyrael.kharazim.chat.simple;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author Tyrael Archangel
 * @since 2023/12/29
 */
public class Server implements Closeable {

    private final int port;
    private ServerSocket serverSocket;
    private volatile boolean shutdown = false;

    public Server(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        this.serverSocket = new ServerSocket(port);
        while (true) {
            Socket socket;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                if (!this.shutdown) {
                    e.printStackTrace();
                }
                return;
            }
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            int len;
            StringBuilder sb = new StringBuilder();
            while ((len = inputStream.read(bytes)) != -1) {
                sb.append(new String(bytes, 0, len, StandardCharsets.UTF_8));
            }
            System.out.println("server received message: " + sb);
            inputStream.close();
            socket.close();
        }
    }

    @Override
    public void close() throws IOException {
        this.shutdown = true;
        if (this.serverSocket != null) {
            serverSocket.close();
        }
    }
}
