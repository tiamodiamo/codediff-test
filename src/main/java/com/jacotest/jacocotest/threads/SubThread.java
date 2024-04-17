package com.jacotest.jacocotest.threads;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubThread implements Runnable{
    @Override
    public void run() {
        log.info("当前ThreadNameis: " + Thread.currentThread().getName());
        log.info("当前ThreadGroupis: " + Thread.currentThread().getThreadGroup().getName());
    }
}
