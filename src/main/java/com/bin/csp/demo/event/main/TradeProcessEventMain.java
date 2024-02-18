package com.bin.csp.demo.event.main;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.bin.csp.demo.event.dto.Trade;
import com.bin.csp.demo.event.handler.*;
import com.bin.csp.demo.event.producer.TradePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TradeProcessEventMain {
    private static final Logger logger = LoggerFactory.getLogger(TradeWorkerPoolMain.class);
    public static void main(String[] args) throws InterruptedException {

        long beginTime=System.currentTimeMillis();
        int bufferSize=1024;
        ExecutorService executor= Executors.newFixedThreadPool(8);

        Disruptor<Trade> disruptor = new Disruptor<Trade>(new EventFactory<Trade>() {
            @Override
            public Trade newInstance() {
                return new Trade();
            }
        }, bufferSize, executor, ProducerType.SINGLE, new BusySpinWaitStrategy());

//        //菱形操作
//        //使用disruptor创建消费者组C1,C2
//        EventHandlerGroup<Trade> handlerGroup = disruptor.handleEventsWith(new TradeHandlers1(), new TradeHandlers2());
//        //声明在C1,C2完事之后执行JMS消息发送操作 也就是流程走到C3
//        handlerGroup.then(new TradeHandlers3());

//        //顺序操作
//        disruptor.handleEventsWith(new TradeHandlers1()).
//        handleEventsWith(new TradeHandlers2()).
//        handleEventsWith(new TradeHandlers3());


        //六边形操作.
        TradeHandlers1 h1 = new TradeHandlers1();
        TradeHandlers2 h2 = new TradeHandlers2();
        TradeHandlers3 h3 = new TradeHandlers3();
        TradeHandlers4 h4 = new TradeHandlers4();
        TradeHandlers5 h5 = new TradeHandlers5();
        disruptor.handleEventsWith(h1, h2);
        disruptor.after(h1).handleEventsWith(h4);
        disruptor.after(h2).handleEventsWith(h5);
        disruptor.after(h4, h5).handleEventsWith(h3);




        disruptor.start();//启动
        CountDownLatch latch=new CountDownLatch(1);
        //生产者准备
        executor.submit(new TradePublisher(latch, disruptor));

        latch.await();//等待生产者完事.

        disruptor.shutdown();
        executor.shutdown();
        logger.info("总耗时:"+(System.currentTimeMillis()-beginTime));
    }

}
