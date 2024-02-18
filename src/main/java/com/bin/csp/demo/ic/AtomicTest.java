package com.bin.csp.demo.ic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

public class AtomicTest {
    private static final Logger logger = LoggerFactory.getLogger(AtomicTest.class);
    /*
    private static AtomicInteger index = new AtomicInteger(10);
    public static void main(String[] args) {
        new Thread(() -> {
            index.compareAndSet(10, 11);
            index.compareAndSet(11, 10);
            logger.info(Thread.currentThread().getName()+
                    "： 10->11->10");
        },"张三").start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                boolean isSuccess = index.compareAndSet(10, 12);
                logger.info(Thread.currentThread().getName()+
                        "： index是预期的10嘛，"+isSuccess
                        +"   设置的新值是："+index.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"李四").start();
    }
    */
    private static AtomicInteger index = new AtomicInteger(10);
    static AtomicStampedReference<Integer> stampRef
            = new AtomicStampedReference<>(10, 1);
    public static void main(String[] args) {
        new Thread(() -> {
            int stamp = stampRef.getStamp();
            logger.info(Thread.currentThread().getName()
                    + " 第1次版本号： " + stamp);
            stampRef.compareAndSet(10, 11,stampRef.getStamp(),stampRef.getStamp()+1);
            logger.info(Thread.currentThread().getName()
                    + " 第2次版本号： " + stampRef.getStamp());
            stampRef.compareAndSet(11, 10,stampRef.getStamp(),stampRef.getStamp()+1);
            logger.info(Thread.currentThread().getName()
                    + " 第3次版本号： " + stampRef.getStamp());
        },"张三").start();

        new Thread(() -> {
            try {
                int stamp = stampRef.getStamp();
                logger.info(Thread.currentThread().getName()
                        + " 第1次版本号： " + stamp);
                TimeUnit.SECONDS.sleep(2);
                boolean isSuccess =stampRef.compareAndSet(10, 12,
                        stampRef.getStamp(),stampRef.getStamp()+1);
                logger.info(Thread.currentThread().getName()
                        + " 修改是否成功： "+ isSuccess+" 当前版本 ：" + stampRef.getStamp());
                logger.info(Thread.currentThread().getName()
                        + " 当前实际值： " + stampRef.getReference());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"李四").start();
    }

}
