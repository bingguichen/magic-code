package com.bin.csp.demo.event.factory;

import com.lmax.disruptor.EventFactory;
import com.bin.csp.demo.event.dto.OrderEvent;

/**
 * 2、事件工厂
 */
public class OrderEventFactory implements EventFactory<OrderEvent> {
    @Override
    public OrderEvent newInstance() {
        return new OrderEvent();
    }
}
