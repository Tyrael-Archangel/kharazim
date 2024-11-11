package com.tyrael.kharazim.application.user.service.component;

import com.tyrael.kharazim.common.exception.ShouldNotHappenException;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Tyrael Archangel
 * @since 2023/12/25
 */
@Component
public class PasswordEncoder {

    private final MessageDigest md5;
    private final Random random;
    private final int saltLength;
    private final ByteEncoder byteEncoder;

    public PasswordEncoder() {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new ShouldNotHappenException(e);
        }
        saltLength = 16;
        random = ThreadLocalRandom.current();
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
        random.nextBytes(saltBytes);

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
        byte[] rawPasswordBytes = rawPassword.getBytes();
        byte[] rawPasswordAndSaltBytes = merge(rawPasswordBytes, saltBytes);
        return md5.digest(rawPasswordAndSaltBytes);
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
            for (byte aByte : bytes) {
                int digital = aByte;

                if (digital < 0) {
                    digital += 256;
                }
                if (digital < 16) {
                    hexBuilder.append("0");
                }
                hexBuilder.append(Integer.toHexString(digital));
            }
            return hexBuilder.toString();
        }

        public byte[] parse(String str) {
            byte[] bytes = new byte[str.length() / 2];
            for (int i = 0; i < str.length(); i += 2) {
                int b = Integer.parseUnsignedInt(str.substring(i, i + 2), 16);
                if (b > 127) {
                    b -= 256;
                }
                bytes[i / 2] = (byte) b;
            }
            return bytes;
        }
    }

}
