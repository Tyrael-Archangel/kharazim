package com.tyrael.kharazim.demo.chat.house;

import java.io.IOException;
import java.net.Socket;

/**
 * @author Tyrael Archangel
 * @since 2023/12/29
 */
public class Client {

    private final String serverHost;
    private final int serverPort;
    private final String name;
    private Socket socket;

    public Client(String serverHost, int serverPort, String name) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.name = name;
    }

    public void start() throws IOException {
        this.socket = new Socket(serverHost, serverPort);
        new Thread(() -> {
            while (!this.socket.isClosed()) {
                try {
                    MsgBody msgBody = SendAndReadMsgUtil.readBody(this.socket.getInputStream());
                    if (msgBody != null) {
                        System.out.println(msgBody.msg());
                    }
                } catch (IOException e) {
                    return;
                }
            }
        }).start();
    }

    public void join() throws IOException {
        SendAndReadMsgUtil.sendMsg(socket.getOutputStream(), MsgType.JOIN, name);
    }

    public void leave() throws IOException {
        SendAndReadMsgUtil.sendMsg(socket.getOutputStream(), MsgType.LEAVE);
        socket.close();
    }

    public void sendP2pMsg(String target, String msg) throws IOException {
        SendAndReadMsgUtil.sendMsg(socket.getOutputStream(), MsgType.P2P, new P2pMsg(target, msg).toMsg());
    }

    public void sendBroadcastMsg(String msg) throws IOException {
        SendAndReadMsgUtil.sendMsg(socket.getOutputStream(), MsgType.BROADCAST, msg);
    }

}
