package com.bin.csp.demo.event.handler;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import com.bin.csp.demo.event.dto.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

//消费者
public class TradeHandler implements EventHandler<Trade>, WorkHandler<Trade> {
    private static final Logger logger = LoggerFactory.getLogger(TradeHandler.class);
    @Override
    public void onEvent(Trade event, long sequence, boolean  endOfBatch) throws Exception {
        this.onEvent(event);
    }

    @Override
    public void onEvent(Trade event) throws Exception {
        //这里做具体的消费逻辑
        event.setId(UUID.randomUUID().toString());//简单生成下ID
        logger.info("EventHandler 消费者"+ Thread.currentThread().getName()
                +"获取数据id:"+ event.getId()+",name:"+event.getName()+",count:"+event.getCount()+",price:"+event.getPrice());
    }
}
