package com.tyrael.kharazim.application.user.service.component;

import com.tyrael.kharazim.common.exception.ShouldNotHappenException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Component
public class PasswordEncoder {

    private final int saltLength;
    private final ByteEncoder byteEncoder;

    public PasswordEncoder() {
        saltLength = 16;
        byteEncoder = new ByteEncoder();
    }

    /**
     * Encode the raw password
     *
     * @param rawPassword 原始密码
     * @return encodedPassword
     */
    public String encode(String rawPassword) {

        byte[] saltBytes = new byte[saltLength];
        ThreadLocalRandom.current().nextBytes(saltBytes);

        byte[] encodedBytes = encode(rawPassword, saltBytes);

        byte[] encodedAndSaltBytes = merge(encodedBytes, saltBytes);
        return byteEncoder.toString(encodedAndSaltBytes);
    }

    /**
     * Verify the encoded password obtained from storage matches the submitted raw password after it too is encoded.
     *
     * @param rawPassword     the raw password to encode and match
     * @param encodedPassword the encoded password from storage to compare with
     * @return true or false
     */
    public boolean matches(String rawPassword, String encodedPassword) {

        try {
            byte[] encodedPasswordBytes = byteEncoder.parse(encodedPassword);

            byte[] analyticEncodedBytes = new byte[encodedPasswordBytes.length - saltLength];
            byte[] analyticSaltBytes = new byte[saltLength];
            System.arraycopy(encodedPasswordBytes, 0, analyticEncodedBytes, 0, analyticEncodedBytes.length);
            System.arraycopy(encodedPasswordBytes, encodedPasswordBytes.length - saltLength, analyticSaltBytes, 0, saltLength);

            byte[] encodedBytes = encode(rawPassword, analyticSaltBytes);

            return Arrays.equals(analyticEncodedBytes, encodedBytes);

        } catch (Exception e) {
            return false;
        }
    }

    private byte[] encode(String rawPassword, byte[] saltBytes) {
        byte[] rawPasswordBytes = rawPassword.getBytes(StandardCharsets.UTF_8);
        byte[] rawPasswordAndSaltBytes = merge(rawPasswordBytes, saltBytes);
        try {
            return MessageDigest.getInstance("MD5").digest(rawPasswordAndSaltBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new ShouldNotHappenException(e);
        }
    }

    private byte[] merge(byte[] a, byte[] b) {
        byte[] encodedPasswordBytes = new byte[a.length + b.length];
        System.arraycopy(a, 0, encodedPasswordBytes, 0, a.length);
        System.arraycopy(b, 0, encodedPasswordBytes, a.length, b.length);
        return encodedPasswordBytes;
    }

    private static class ByteEncoder {

        public String toString(byte[] bytes) {
            StringBuilder hexBuilder = new StringBuilder();
            for (byte b : bytes) {
                hexBuilder.append(String.format("%02x", b & 0xFF));
            }
            return hexBuilder.toString();
        }

        public byte[] parse(String str) {
            byte[] bytes = new byte[str.length() / 2];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
            }
            return bytes;
        }
    }

}
