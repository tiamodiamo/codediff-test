package com.jacotest.jacocotest.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DeCompileCode {
    private String JarDir;
    private String jarOutputDir;

    private String jadxJarPaht = "D:\\workspace\\codediff-test\\src\\main\\java\\com\\jacotest\\lib\\jadx-cli-dev.jar";


    public DeCompileCode(String JarDir, String jarOutputDir) {
        this.JarDir = JarDir;
        this.jarOutputDir = jarOutputDir;
    }


    public void decompileJar() throws IOException, InterruptedException, IOException {
        String jarPath = this.JarDir;
        String outputDir = this.jarOutputDir;

        ProcessBuilder processBuilder = new ProcessBuilder("java -jar",jadxJarPaht, "-d", outputDir, jarPath);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("JADX command failed with exit code " + exitCode);
        }
    }
}
