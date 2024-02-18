package com.bin.csp.demo.trace;

import java.util.Map;

/**
 * @author BinChan
 */
public class LoggerVo {
    private String reqId;
    private String ip;
    private String url;
    private Map<String, String> params;
    private Long start;
    private long executeTime = -1;
    private Object result;

    public LoggerVo() {
    }

    public LoggerVo(String reqId) {
        this.reqId = reqId;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public long getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(long executeTime) {
        this.executeTime = executeTime;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        if (result instanceof WrapperResponse) {
            return "{ip=" + ip + ",url=" + url + ",params=" + params + ",cost=" + executeTime + "}";
        } else {
            return "{ip=" + ip + ",url=" + url + ",params=" + params + ",cost=" + executeTime + ",result=" + result + "}";
        }
    }


}
