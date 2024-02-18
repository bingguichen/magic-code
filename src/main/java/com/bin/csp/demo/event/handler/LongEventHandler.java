package com.bin.csp.demo.event.handler;

import com.lmax.disruptor.EventHandler;
import com.bin.csp.demo.event.dto.LongEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//3、我们还需要一个事件消费者，也就是一个事件处理器。这个事件处理器简单地把事件中存储的数据打印到终端：

public class LongEventHandler implements EventHandler<LongEvent> {
    private static final Logger logger = LoggerFactory.getLogger(LongEventHandler.class);
    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) {
        //消费逻辑
        logger.info("Event: {}", event);
    }
}
