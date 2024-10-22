package com.jacotest.jacocotest.controller;

import java.io.File;
import java.io.IOException;

/**
 * FileName: ClassDecompiler
 * Author:   likai
 * Date:     2024/10/21 11:12
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class ClassDecompiler {
    public static void decompileClassFile(File classFile, File outputDir) throws IOException, InterruptedException {
        // 设置 CFR 反编译器的路径
        String cfrJarPath = "D:\\workspace\\codediff-test\\src\\main\\java\\com\\jacotest\\lib\\cfr.jar";  // CFR JAR 的路径

        // 构建命令行命令
        ProcessBuilder processBuilder = new ProcessBuilder(
                "java", "-jar", cfrJarPath, classFile.getAbsolutePath(), "--outputdir", outputDir.getAbsolutePath());

        // 启动进程并执行反编译
        Process process = processBuilder.start();
        int exitCode = process.waitFor();  // 等待进程执行完成

        if (exitCode == 0) {
            System.out.println("Decompiled: " + classFile.getName() + " to " + outputDir.getAbsolutePath());
        } else {
            System.err.println("Failed to decompile: " + classFile.getName());
        }
    }
}