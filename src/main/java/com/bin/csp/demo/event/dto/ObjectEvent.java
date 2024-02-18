package com.bin.csp.demo.event.dto;

public class ObjectEvent<T> {
    T val;

    public void clear()
    {
        val = null;
    }
}
