package com.bin.csp.demo.ic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VolatileExample {
    private static final Logger logger = LoggerFactory.getLogger(VolatileExample.class);
    public static volatile int count = 0; // 计数器
    public static final int size = 100000; // 循环测试次数

    public static void main(String[] args) {
        // ++ 方式 10w 次
        Thread thread = new Thread(() -> {
            for (int i = 1; i <= size; i++) {
                count++;
            }
        });
        thread.start();
        // -- 10w 次
        for (int i = 1; i <= size; i++) {
            count--;
        }
        // 等所有线程执行完成
        while (thread.isAlive()) {}
        logger.info("["+count+"]"); // 打印结果
    }
}
