package com.tyrael.kharazim.demo.chat.house;

import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author Tyrael Archangel
 * @since 2023/12/29
 */
public class ServerStarterTest {

    @Test
    public void startServer() throws IOException {
        Server server = new Server(9408);
        server.start();
    }

}
