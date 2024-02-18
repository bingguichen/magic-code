package com.bin.csp.demo.event.handler;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import com.bin.csp.demo.event.dto.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TradeHandlers1 implements EventHandler<Trade>, WorkHandler<Trade> {
    private static final Logger logger = LoggerFactory.getLogger(TradeHandlers1.class);
    @Override
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
        this.onEvent(event);
    }

    @Override
    public void onEvent(Trade event) throws Exception {
        logger.info("handler1: set name");
        event.setName("h1");
        Thread.sleep(2000);
    }

}

