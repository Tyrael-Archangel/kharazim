package com.tyrael.kharazim.demo;

import com.tyrael.kharazim.common.util.RandomStringUtil;
import com.tyrael.kharazim.demo.chat.house.BytesUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * @author Tyrael Archangel
 * @since 2024/11/25
 */
@SuppressWarnings("all")
public class EncryptAndDecryptImageDemo {

    public static void main(String[] args) throws IOException {

        File trueFileDir = new File("/Users/jay/Desktop/temp/MhR6wo6IPT_decrypt");
        File encryptFileDir = new File("/Users/jay/Desktop/temp/MhR6wo6IPT");

//        encrypt(trueFileDir, encryptFileDir);

//        decrypt(encryptFileDir, trueFileDir);
    }

    private static void encrypt(File sourceDir, File targetDir) throws IOException {

        if (!sourceDir.exists()) {
            throw new FileNotFoundException(sourceDir.getAbsolutePath());
        }
        if (targetDir.exists()) {
            throw new IllegalArgumentException("targetDir already exists");
        }
        if (!sourceDir.isDirectory()) {
            throw new IllegalArgumentException("sourceDir must be a directory");
        }

        boolean dirCreated = targetDir.mkdir();
        if (!dirCreated) {
            throw new IllegalArgumentException("targetDir directory could not be created");
        }
        File[] children = sourceDir.listFiles();
        if (children != null) {
            for (File child : children) {
                encryptFile(child, targetDir);
            }
        }
    }

    private static void encryptFile(File sourceFile, File targetDir) throws IOException {

        String targetFileName = RandomStringUtil.make(16) + ".encrypt";
        File targetFile = new File(targetDir, targetFileName);

        try (InputStream inputStream = Files.newInputStream(sourceFile.toPath());
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             OutputStream targetFileOutputStream = Files.newOutputStream(targetFile.toPath())) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }

            String sourceFileName = sourceFile.getName();

            byte[] fileNameBytes = sourceFileName.getBytes(StandardCharsets.UTF_8);
            byte[] fileNameLengthBytes = BytesUtil.toBytes(fileNameBytes.length);

            targetFileOutputStream.write(fileNameLengthBytes);
            targetFileOutputStream.write(fileNameBytes);

            byte[] bytes = byteArrayOutputStream.toByteArray();
            reverseBytes(bytes);
            targetFileOutputStream.write(bytes);
        }
    }

    private static void decrypt(File sourceDir, File targetDir) throws IOException {
        if (!sourceDir.exists()) {
            throw new FileNotFoundException(sourceDir.getAbsolutePath());
        }
        if (targetDir.exists()) {
            throw new IllegalArgumentException("targetDir already exists");
        }
        if (!sourceDir.isDirectory()) {
            throw new IllegalArgumentException("sourceDir must be a directory");
        }
        boolean dirCreated = targetDir.mkdir();
        if (!dirCreated) {
            throw new IllegalArgumentException("targetDir directory could not be created");
        }
        File[] children = sourceDir.listFiles();
        if (children != null) {
            for (File child : children) {
                decryptFile(child, targetDir);
            }
        }

    }

    private static void decryptFile(File sourceFile, File targetDir) throws IOException {
        try (InputStream inputStream = Files.newInputStream(sourceFile.toPath());
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] fileNameLengthBytes = new byte[BytesUtil.BYTE_SIZE];
            int read = inputStream.read(fileNameLengthBytes);
            if (read == -1) {
                throw new IOException("Incorrect file format");
            }

            int fileNameLength = BytesUtil.toInt(fileNameLengthBytes);
            byte[] fileNameBytes = new byte[fileNameLength];
            read = inputStream.read(fileNameBytes);
            if (read == -1) {
                throw new IOException("Incorrect file format");
            }

            String fileName = new String(fileNameBytes, StandardCharsets.UTF_8);
            File targetFile = new File(targetDir, fileName);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }

            byte[] bytes = byteArrayOutputStream.toByteArray();
            reverseBytes(bytes);

            Files.write(targetFile.toPath(), bytes);
        }
    }

    private static void reverseBytes(byte[] bytes) {
        int left = 0;
        int right = bytes.length - 1;

        while (left < right) {
            byte temp = bytes[left];
            bytes[left] = bytes[right];
            bytes[right] = temp;

            left++;
            right--;
        }
    }

}
