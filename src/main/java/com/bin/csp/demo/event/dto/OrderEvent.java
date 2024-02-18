package com.bin.csp.demo.event.dto;

/**
 * 1、消息载体(事件)
 */
public class OrderEvent {
    private long value;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
