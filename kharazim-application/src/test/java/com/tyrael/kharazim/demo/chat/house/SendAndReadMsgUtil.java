package com.tyrael.kharazim.demo.chat.house;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author Tyrael Archangel
 * @since 2023/12/29
 */
public class SendAndReadMsgUtil {

    public static void sendMsg(OutputStream outputStream, MsgType type, String msg) throws IOException {
        MsgBody msgBody = new MsgBody(type, msg);
        String body = msgBody.toBody();

        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);
        byte[] bodyLengthBytes = BytesUtil.toBytes(bodyBytes.length);
        outputStream.write(BytesUtil.mergeBytes(bodyLengthBytes, bodyBytes));
        outputStream.flush();
    }

    public static void sendMsg(OutputStream outputStream, MsgType type) throws IOException {
        sendMsg(outputStream, type, null);
    }

    public static MsgBody readBody(InputStream inputStream) throws IOException {
        byte[] bodySizeBytes = new byte[4];
        int read = inputStream.read(bodySizeBytes);
        if (read == -1) {
            return null;
        }
        int bodySize = BytesUtil.toInt(bodySizeBytes);
        byte[] bodyBytes = new byte[bodySize];
        read = inputStream.read(bodyBytes);
        if (read == -1) {
            return null;
        }
        String body = new String(bodyBytes, StandardCharsets.UTF_8);
        return MsgBody.convertBody(body);
    }

}
