package com.tyrael.kharazim.chat.house;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author Tyrael Archangel
 * @since 2023/12/29
 */
public class ClientStarter {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入用户名");
        String name = scanner.nextLine();

        Client client = new Client("localhost", 9408, name);
        client.start();
        client.join();

        while (true) {
            String msg = scanner.nextLine();
            if (!msg.isBlank()) {
                if ("exit".equalsIgnoreCase(msg)) {
                    client.leave();
                    return;
                }

                int firstBlank = msg.indexOf(" ");
                if (firstBlank == -1) {
                    client.sendBroadcastMsg(msg);
                } else {
                    String target = msg.substring(0, firstBlank);
                    String targetMsg = msg.substring(firstBlank + 1);
                    client.sendP2pMsg(target, targetMsg);
                }

            }
        }
    }

}
