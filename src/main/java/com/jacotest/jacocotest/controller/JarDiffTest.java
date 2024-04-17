package com.jacotest.jacocotest.controller;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class JarDiffTest {

    public static void main(String[] args) throws Exception {
        File jarFile1 = new File("D:\\workspace\\codediff-test\\src\\main\\java\\com\\jacotest\\lib\\jacocotest-0.0.1-copy-SNAPSHOT.jar");
        File jarFile2 = new File("D:\\workspace\\codediff-test\\src\\main\\java\\com\\jacotest\\lib\\jacocotest-0.0.1-SNAPSHOT.jar");

        String digest1 = calculateDigest(jarFile1, "MD5");
        String digest2 = calculateDigest(jarFile2, "MD5");

        System.out.println("MD5 of jar1: " + digest1);
        System.out.println("MD5 of jar2: " + digest2);

        if (digest1.equals(digest2)) {
            System.out.println("JAR files are identical.");
        } else {
            System.out.println("JAR files are different.");
        }
    }

    private static String calculateDigest(File file, String algorithm) throws Exception {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        FileInputStream fis = new FileInputStream(file);

        byte[] byteArray = new byte[1024];
        int bytesCount;

        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        }

        fis.close();
        byte[] mdbytes = digest.digest();

        StringBuilder hexString = new StringBuilder();
        for (byte mdbyte : mdbytes) {
            hexString.append(Integer.toString((mdbyte & 0xff) + 0x100, 16).substring(1));
        }

        return hexString.toString();
    }
}