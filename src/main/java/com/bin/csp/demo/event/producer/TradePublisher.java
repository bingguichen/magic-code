package com.bin.csp.demo.event.producer;

import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;
import com.bin.csp.demo.event.dto.Trade;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

// 2、生产者
public class TradePublisher implements Runnable {
    Disruptor<Trade> disruptor;
    private CountDownLatch latch;

    private static int LOOP=10;//模拟百万次交易的发生

    public TradePublisher(CountDownLatch latch,Disruptor<Trade> disruptor) {
        this.disruptor=disruptor;
        this.latch=latch;
    }

    @Override
    public void run() {
        TradeEventTranslator tradeTransloator = new TradeEventTranslator();
        for(int i=0;i<LOOP;i++){
            disruptor.publishEvent(tradeTransloator);
        }
        latch.countDown();
    }
}

class TradeEventTranslator implements EventTranslator<Trade> {

    private Random random=new Random();

    @Override
    public void translateTo(Trade event, long sequence) {
        this.generateTrade(event);
    }

    private Trade generateTrade(Trade trade){
        trade.setPrice(random.nextDouble()*9999);
        return trade;
    }

}
