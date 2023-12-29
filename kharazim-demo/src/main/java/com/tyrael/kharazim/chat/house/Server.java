package com.tyrael.kharazim.chat.house;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Tyrael Archangel
 * @since 2023/12/29
 */
public class Server {

    private final Map<Socket, String> clientsNames = new HashMap<>();
    private final Map<String, Socket> clients = new HashMap<>();
    private final int port;
    private final ExecutorService executorService;

    public Server(int port) {
        this.port = port;
        this.executorService = new ThreadPoolExecutor(
                10,
                10,
                0L,
                TimeUnit.MILLISECONDS,
                new SynchronousQueue<>(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                final Socket socket;
                try {
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                executorService.execute(() -> handleClient(socket));
            }
        }
    }

    private void handleClient(Socket socket) {
        try {
            InputStream inputStream = socket.getInputStream();
            while (!socket.isClosed()) {
                MsgBody msgBody = SendAndReadMsgUtil.readBody(inputStream);
                if (msgBody == null) {
                    return;
                }
                handleMsg(socket, msgBody);
            }
        } catch (IOException e) {
            removeClient(socket);
        }
    }

    private void handleMsg(Socket socket, MsgBody msgBody) throws IOException {
        switch (msgBody.type()) {
            case JOIN -> handleJoin(socket, msgBody.msg());
            case LEAVE -> handleLeave(socket, msgBody.msg());
            case BROADCAST -> handleBroadcast(socket, msgBody.msg());
            case P2P -> handleP2p(socket, msgBody.msg());
        }
    }

    public void handleJoin(Socket socket, String clientName) throws IOException {
        int maxClientSize = 5;
        synchronized (clients) {
            if (clients.size() >= maxClientSize) {
                OutputStream outputStream = socket.getOutputStream();
                SendAndReadMsgUtil.sendMsg(outputStream, MsgType.JOIN, "加入失败");
            } else {
                clients.put(clientName, socket);
                clientsNames.put(socket, clientName);
                String msg = "[" + clientName + "]加入群聊";
                System.out.println(msg);
                sendBroadcastMsg(msg, socket);
            }
        }
    }

    public void handleLeave(Socket socket, String leaveMsg) throws IOException {
        String clientName = this.removeClient(socket);

        String msg;
        if (leaveMsg != null && !leaveMsg.isBlank()) {
            msg = "[" + clientName + "]退出群聊，并说：" + leaveMsg;
        } else {
            msg = "[" + clientName + "]退出群聊";
        }
        System.out.println(msg);
        sendBroadcastMsg(msg, socket);
    }

    private String removeClient(Socket socket) {
        String clientName;
        Socket clientSocket;
        synchronized (clients) {
            clientName = clientsNames.remove(socket);
            clientSocket = clients.remove(clientName);
        }

        if (clientSocket != null && !clientSocket.isClosed()) {
            try {
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return clientName;
    }

    public void handleP2p(Socket socket, String msg) throws IOException {
        P2pMsg p2pMsg = P2pMsg.convert(msg);
        String targetClient = p2pMsg.target();
        Socket targetSocket = clients.get(targetClient);
        String sourceClientName = clientsNames.get(socket);
        String targetMsg = "[" + sourceClientName + "]对您说: " + p2pMsg.msg();
        SendAndReadMsgUtil.sendMsg(targetSocket.getOutputStream(), MsgType.P2P, targetMsg);
    }

    public void handleBroadcast(Socket socket, String msg) throws IOException {
        String clientName = clientsNames.get(socket);
        sendBroadcastMsg("[" + clientName + "]" + "说: " + msg, socket);
    }

    private void sendBroadcastMsg(String msg, Socket excludedSocket) throws IOException {
        for (Socket socket : clientsNames.keySet()) {
            if (!socket.equals(excludedSocket)) {
                SendAndReadMsgUtil.sendMsg(socket.getOutputStream(), MsgType.JOIN, msg);
            }
        }
    }

}
