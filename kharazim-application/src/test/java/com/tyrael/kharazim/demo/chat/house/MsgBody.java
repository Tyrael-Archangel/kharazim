package com.tyrael.kharazim.demo.chat.house;

/**
 * @author Tyrael Archangel
 * @since 2023/12/29
 */
public record MsgBody(MsgType type, String msg) {

    private static final String SPLIT = "/";
    private static final String NULL = "0";
    private static final String NOT_NULL = "1";

    public static MsgBody convertBody(String body) {
        int firstSplit = body.indexOf(SPLIT);
        MsgType type = MsgType.valueOf(body.substring(0, firstSplit));
        String ifNull = body.substring(firstSplit + SPLIT.length(), firstSplit + SPLIT.length() + 1);
        if (NULL.equals(ifNull)) {
            return new MsgBody(type, null);
        } else {
            String msg = body.substring(firstSplit + SPLIT.length() + 2);
            return new MsgBody(type, msg);
        }
    }

    public static void main(String[] args) {
        MsgBody msgBody = new MsgBody(MsgType.JOIN, null);
        System.out.println(msgBody);
        String body = msgBody.toBody();
        System.out.println(body);
        MsgBody convertBody = MsgBody.convertBody(body);
        System.out.println(convertBody);
    }

    public String toBody() {
        if (msg == null) {
            return type + SPLIT + NULL;
        } else {
            return type + SPLIT + NOT_NULL + SPLIT + msg;
        }
    }

}
