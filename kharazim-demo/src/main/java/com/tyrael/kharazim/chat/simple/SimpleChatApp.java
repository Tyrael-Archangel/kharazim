package com.tyrael.kharazim.chat.simple;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author Tyrael Archangel
 * @since 2023/12/29
 */
public class SimpleChatApp {

    public static void main(String[] args) throws IOException {

        int port = 9408;

        try (Server server = new Server(port)) {
            Thread serverThread = new Thread(() -> {
                try {
                    server.start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            serverThread.start();

            Client client = new Client("localhost", port);

            Scanner scanner = new Scanner(System.in);
            while (true) {
                String msg = scanner.nextLine();
                if ("exit".equalsIgnoreCase(msg)) {
                    break;
                } else {
                    client.sendMsg(msg);
                }
            }
        }

    }

}
