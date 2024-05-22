package com.tyrael.kharazim.demo.chat.house;

/**
 * @param target targetClient
 * @param msg    msg
 * @author Tyrael Archangel
 * @since 2023/12/29
 */
public record P2pMsg(String target, String msg) {

    private static final String SPLIT = "==";

    public static P2pMsg convert(String p2pMsg) {
        int i = p2pMsg.indexOf(SPLIT);
        String target = p2pMsg.substring(0, i);
        String msg = p2pMsg.substring(i + SPLIT.length());
        return new P2pMsg(target, msg);
    }

    public static void main(String[] args) {
        P2pMsg p2pMsg = new P2pMsg("泰瑞尔", "你好");
        System.out.println(p2pMsg);
        String msgBody = p2pMsg.toMsg();
        System.out.println(msgBody);
        P2pMsg convert = P2pMsg.convert(msgBody);
        System.out.println(convert);
    }

    public String toMsg() {
        return target + SPLIT + msg;
    }

}
