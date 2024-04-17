package com.jacotest.jacocotest.controller;

import org.benf.cfr.reader.api.CfrDriver;

import java.io.IOException;
import java.util.Collections;

public class CodeDiffByJar {


    public static void main(String[] args) throws IOException, InterruptedException {

        String oldJarPath = "D:\\workspace\\codediff-test\\src\\main\\java\\com\\jacotest\\lib\\jacocotest-0.0.1-base-SNAPSHOT.jar";
        String oldJarDecomPath = "D:\\workspace\\codediff-test\\src\\main\\java\\com\\jacotest\\output\\old";

        String newJarPath = "D:\\workspace\\codediff-test\\src\\main\\java\\com\\jacotest\\lib\\jacocotest-0.0.1-SNAPSHOT.jar";
        String newJarDecomPath = "D:\\workspace\\codediff-test\\src\\main\\java\\com\\jacotest\\output\\new";
//
//        DeCompileCode oldDec = new DeCompileCode(oldJarPath,oldJarDecomPath);
//        DeCompileCode newDec = new DeCompileCode(newJarPath,newJarDecomPath);
//
//
//
//        oldDec.decompileJar();
//
//        newDec.decompileJar();



        String jarFilePath = "path/to/your/jar/file.jar";

        // 创建一个 CfrDriver 实例
        CfrDriver driver = new CfrDriver.Builder()
                .withOutputSink(new SinkImpl()) // 指定输出的 Sink，这里使用自定义的 SinkImpl
                .build();

        // 反编译指定的 JAR 文件
        driver.analyse(Collections.singletonList(oldJarPath));
    }


    }








