package com.bin.csp.demo.event.handler;

import com.lmax.disruptor.EventHandler;
import com.bin.csp.demo.event.dto.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TradeHandlers2 implements EventHandler<Trade> {
    private static final Logger logger = LoggerFactory.getLogger(TradeHandlers2.class);

    @Override
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
        logger.info("handler2: set price");
        event.setPrice(17.0);
        Thread.sleep(2000);
    }

}
