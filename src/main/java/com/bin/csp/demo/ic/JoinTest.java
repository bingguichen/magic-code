package com.bin.csp.demo.ic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JoinTest {
    public static void main(String [] args) throws InterruptedException {
        ThreadJoinTest t1 = new ThreadJoinTest("小明");
        ThreadJoinTest t2 = new ThreadJoinTest("小东");
        t1.start();
        /**join的意思是使得放弃当前线程的执行，并返回对应的线程，例如下面代码的意思就是：
         程序在main线程中调用t1线程的join方法，则main线程放弃cpu控制权，并返回t1线程继续执行直到线程t1执行完毕
         所以结果是t1线程执行完后，才到主线程执行，相当于在main线程中同步t1线程，t1执行完了，main线程才有执行的机会
         */
        t1.join(10);
        t2.start();
    }

}
class ThreadJoinTest extends Thread{
    private static final Logger logger = LoggerFactory.getLogger(ThreadJoinTest.class);
    public ThreadJoinTest(String name){
        super(name);
    }
    @Override
    public void run(){
        for(int i=0;i<1000;i++){
            logger.info(this.getName() + ":" + i);
        }
    }
}
