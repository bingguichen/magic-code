package com.bin.csp.demo.event.factory;

import com.lmax.disruptor.EventFactory;
import com.bin.csp.demo.event.dto.LongEvent;

// 2、需要让disruptor为我们创建事件，我们同时还声明了一个EventFactory来实例化Event对象。
public class LongEventFactory implements EventFactory<LongEvent> {
    @Override
    public LongEvent newInstance() {
        return new LongEvent();
    }
}
