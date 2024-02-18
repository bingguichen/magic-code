package com.bin.csp.demo.event.handler;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import com.bin.csp.demo.event.dto.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TradeHandlers3 implements EventHandler<Trade>, WorkHandler<Trade> {
    private static final Logger logger = LoggerFactory.getLogger(TradeHandlers3.class);
    @Override
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
        this.onEvent(event);
    }

    @Override
    public void onEvent(Trade event) throws Exception {
        logger.info("handler3: name: " + event.getName() + " , price: " + event.getPrice() + ";  instance: " + event.toString());
    }

}

