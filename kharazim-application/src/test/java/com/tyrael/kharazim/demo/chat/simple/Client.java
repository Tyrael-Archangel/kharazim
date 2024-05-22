package com.tyrael.kharazim.demo.chat.simple;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author Tyrael Archangel
 * @since 2023/12/29
 */
public class Client {

    private final String host;
    private final int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }


    /**
     * send msg
     */
    public void sendMsg(String msg) throws IOException {
        try (Socket socket = new Socket(host, port)) {
            socket.getOutputStream().write(msg.getBytes(StandardCharsets.UTF_8));
        }
    }

}
