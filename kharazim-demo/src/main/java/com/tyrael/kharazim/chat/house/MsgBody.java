package com.tyrael.kharazim.chat.house;

/**
 * @author Tyrael Archangel
 * @since 2023/12/29
 */
public record MsgBody(MsgType type, String msg) {

    private static final String SPLIT = "/";

    public static MsgBody convertBody(String body) {
        int i = body.indexOf(SPLIT);
        MsgType type = MsgType.valueOf(body.substring(0, i));
        String msg = body.substring(i + SPLIT.length());
        return new MsgBody(type, msg);
    }

    public static void main(String[] args) {
        MsgBody msgBody = new MsgBody(MsgType.JOIN, "泰瑞尔");
        System.out.println(msgBody);
        String body = msgBody.toBody();
        System.out.println(body);
        MsgBody convertBody = MsgBody.convertBody(body);
        System.out.println(convertBody);
    }

    public String toBody() {
        return type + SPLIT + msg;
    }

}
