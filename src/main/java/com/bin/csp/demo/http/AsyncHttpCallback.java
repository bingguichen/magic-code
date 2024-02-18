package com.bin.csp.demo.http;


/**
 * 异步请求回调
 *
 * @author BinChan
 */
public abstract class AsyncHttpCallback implements HttpCallback {
    private String reqId;

    public AsyncHttpCallback(String reqId) {
        this.reqId = reqId;
    }

    public String getReqId() {
        return this.reqId;
    }
}
