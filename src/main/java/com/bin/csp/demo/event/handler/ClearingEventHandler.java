package com.bin.csp.demo.event.handler;

import com.lmax.disruptor.EventHandler;
import com.bin.csp.demo.event.dto.ObjectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClearingEventHandler<T> implements EventHandler<ObjectEvent<T>> {
    private static final Logger logger = LoggerFactory.getLogger(ClearingEventHandler.class);
    @Override
    public void onEvent(ObjectEvent<T> event, long sequence, boolean endOfBatch) {
        event.clear();
        logger.info("event cleared.");
    }
}
