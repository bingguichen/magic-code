package com.bin.csp.demo.event.handler;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import com.bin.csp.demo.event.dto.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 4、消费者
 */
public class OrderEventHandler implements EventHandler<OrderEvent>, WorkHandler<OrderEvent> {
    private static final Logger logger = LoggerFactory.getLogger(OrderEventHandler.class);
    @Override
    public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) throws Exception {
        // TODO 消费逻辑
        logger.info("EventHandler 消费者"+ Thread.currentThread().getName()
                +"获取数据value:"+ event.getValue()+",name:"+event.getName());
    }

    @Override
    public void onEvent(OrderEvent event) throws Exception {
        // TODO 消费逻辑
        logger.info("消费者"+ Thread.currentThread().getName()
                +"获取数据value:"+ event.getValue()+",name:"+event.getName());

    }
}
