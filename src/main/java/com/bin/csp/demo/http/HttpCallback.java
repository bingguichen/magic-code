package com.bin.csp.demo.http;


public interface HttpCallback {

    default void failed(Exception e) {
    }

    void completed(R var1);

    default void cancelled() {
    }
}
