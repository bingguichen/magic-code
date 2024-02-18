package com.bin.csp.demo.event.producer;

import com.lmax.disruptor.RingBuffer;
import com.bin.csp.demo.event.dto.OrderEvent;

/**
 * 3、消息（事件）生产者
 */
public class OrderEventProducer {
    //事件队列
    private final RingBuffer<OrderEvent> ringBuffer;

    public OrderEventProducer(RingBuffer<OrderEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(long value,String name) {
        // 获取事件队列 的下一个槽
        long sequence = ringBuffer.next();
        try {
            //获取消息载体（事件）
            OrderEvent orderEvent = ringBuffer.get(sequence);
            // 写入消息数据
            orderEvent.setValue(value);
            orderEvent.setName(name);
        } catch (Exception e) {
            // TODO  异常处理
            e.printStackTrace();
        } finally {
            System.out.println("生产者"+ Thread.currentThread().getName()
                    +"发送数据value:"+value+",name:"+name);
            //发布事件
            ringBuffer.publish(sequence);
        }
    }

}
