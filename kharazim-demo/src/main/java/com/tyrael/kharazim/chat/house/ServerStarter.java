package com.tyrael.kharazim.chat.house;

import java.io.IOException;

/**
 * @author Tyrael Archangel
 * @since 2023/12/29
 */
public class ServerStarter {

    public static void main(String[] args) throws IOException {
        Server server = new Server(9408);
        server.start();
    }

}
