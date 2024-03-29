package com.bin.csp.demo.event.producer;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.bin.csp.demo.event.dto.LongEvent;

import java.nio.ByteBuffer;

/**
 * 很明显的是：当用一个简单队列来发布事件的时候会牵涉更多的细节，这是因为事件对象还需要预先创建。
 * 发布事件最少需要两步：获取下一个事件槽并发布事件（发布事件的时候要使用try/finnally保证事件一定会被发布）。
 * 如果我们使用RingBuffer.next()获取一个事件槽，那么一定要发布对应的事件。
 * 如果不能发布事件，那么就会引起Disruptor状态的混乱。
 * 尤其是在多个事件生产者的情况下会导致事件消费者失速，从而不得不重启应用才能会恢复。
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 */
//4、这是一个生产者
public class LongEventProducer {
    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    private static final EventTranslatorOneArg<LongEvent, ByteBuffer> TRANSLATOR =
            new EventTranslatorOneArg<LongEvent, ByteBuffer>() {
                @Override
                public void translateTo(LongEvent event, long sequence, ByteBuffer bb)
                {
                    event.setValue(bb.getLong(0));
                }
            };

    /**
     * onData用来发布事件，每调用一次就发布一次事件
     * 它的参数会用过事件传递给消费者
     */
    public void onData(ByteBuffer bb) {
        ringBuffer.publishEvent(TRANSLATOR, bb);
//        //1.可以把ringBuffer看做一个事件队列，那么next就是得到下面一个事件槽
//        long sequence = ringBuffer.next();
//        try {
//            //2.用上面的索引取出一个空的事件用于填充（获取该序号对应的事件对象）
//            LongEvent event = ringBuffer.get(sequence);
//            //3.获取要通过事件传递的业务数据
//            event.set(bb.getLong(0));
//        }
//        finally {
//            //4.发布事件，发布后才能消费
//            //注意，最后的 ringBuffer.publish 方法必须包含在  finally 中以确保必须得到调用；如果某个请求的 sequence 未被提交，将会堵塞后续的发布操作或者其它的 producer。
//            ringBuffer.publish(sequence);
//        }
    }

}
