package com.bin.csp.demo.event.main;

import cn.hutool.json.JSONUtil;
import com.lmax.disruptor.*;
import com.bin.csp.demo.event.dto.Trade;
import com.bin.csp.demo.event.handler.TradeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TradeWorkerPoolMain {
    private static final Logger logger = LoggerFactory.getLogger(TradeWorkerPoolMain.class);
    public static void main(String[] args) throws  InterruptedException {
        int BUFFER_SIZE=1024;
        int THREAD_NUMBERS=4;

        EventFactory<Trade> eventFactory = new  EventFactory<Trade>() {
            public Trade newInstance() {
                return new Trade();
            }
        };

        RingBuffer<Trade> ringBuffer =  RingBuffer.createSingleProducer(eventFactory, BUFFER_SIZE);

        SequenceBarrier sequenceBarrier =  ringBuffer.newBarrier();

        ExecutorService executor =  Executors.newFixedThreadPool(THREAD_NUMBERS);

        WorkHandler<Trade> handler = new TradeHandler();
        WorkerPool<Trade> workerPool = new  WorkerPool<Trade>(ringBuffer, sequenceBarrier, new IgnoreExceptionHandler(), handler);

        workerPool.start(executor);

        //下面这个生产N个数据 这里其实应该换成生产者
        for(int i=0;i<10000;i++){
            long seq=ringBuffer.next();
            ringBuffer.get(seq).setPrice(Math.random()*9999);
            ringBuffer.get(seq).setName(Thread.currentThread().getName());
            logger.info("{} job publishing...", JSONUtil.toJsonStr(ringBuffer.get(seq)));
            ringBuffer.publish(seq);
        }

        Thread.sleep(1000);
        workerPool.halt();
        executor.shutdown();
    }

}
