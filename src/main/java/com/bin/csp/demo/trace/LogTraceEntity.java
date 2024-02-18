package com.bin.csp.demo.trace;

import java.util.HashMap;
import java.util.Map;

public class LogTraceEntity {
	private Long requestTime;
	private String appName;
	private String traceId;
	private String tracePath;
	private final Map<String, String> queryData;
	private String clientIp;
	private int clientPort;
	private String serverIp;
	private int serverPort;
	private Long elapsedTime;
	private Long requestLength;
	private Long responseLength;
	private String method;
	private String url;
	private int statusCode;
	private String queryParams;
	private String callMethod;
	private String businessCode;
	private String returnContent;
	
	public LogTraceEntity() {
		this.queryData = new HashMap<>();
	}
	
	public Long getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Long requestTime) {
		this.requestTime = requestTime;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getTraceId() {
		return traceId;
	}
	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}
	public String getTracePath() {
		return tracePath;
	}
	public void setTracePath(String tracePath) {
		this.tracePath = tracePath;
	}
	public String getQueryData() {
		StringBuilder sb = new StringBuilder();
		this.queryData.forEach((key, value) -> sb.append(String.format("%s:%s$", formatStr(key), formatStr(value))));
		if(sb.length() > 0){
			sb.delete(sb.length() - 1, sb.length());
		}
		return sb.toString();
	}
	
	public String getQueryData(String key) {
		return this.queryData.get(key);
	}
	
	private String formatStr(String val) {
		//系统约定的字符，如：‘|’、’#’、’:’、’$’，以及不可见字符，需要将对应字符替换为ASCII的通配符，即@{code}的格式
		return val.replaceAll("\\s", "")
		.replaceAll("\\|", "@{"+((int)'|')+"}")
		.replaceAll("\\#", "@{"+((int)'#')+"}")
		.replaceAll("\\:", "@{"+((int)':')+"}")
		.replaceAll("\\$", "@{"+((int)'$')+"}");
	}
	public Map<String, String> pubQueryData(String key, String queryData) {
		this.queryData.put(key, queryData);
		return this.queryData;
	}
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	public int getClientPort() {
		return clientPort;
	}
	public void setClientPort(int clientPort) {
		this.clientPort = clientPort;
	}
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	public int getServerPort() {
		return serverPort;
	}
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	public Long getElapsedTime() {
		return elapsedTime;
	}
	public void setElapsedTime(Long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
	public Long getRequestLength() {
		return requestLength;
	}
	public void setRequestLength(Long requestLength) {
		this.requestLength = requestLength;
	}
	public Long getResponseLength() {
		return responseLength;
	}
	public void setResponseLength(Long responseLength) {
		this.responseLength = responseLength;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getQueryParams() {
		return queryParams;
	}
	public void setQueryParams(String queryParams) {
		this.queryParams = queryParams;
	}
	public String getCallMethod() {
		return callMethod;
	}
	public void setCallMethod(String callMethod) {
		this.callMethod = callMethod;
	}
	public String getBusinessCode() {
		return businessCode;
	}
	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}
	public String getReturnContent() {
		//TODO:需要处理特殊字符
		return returnContent;
	}
	public void setReturnContent(String returnContent) {
		returnContent = returnContent.replace("\r", "").replace("\n", "");
		if (returnContent.length() > 300) {
			this.returnContent = returnContent.substring(0, 300);
		} else {
			this.returnContent = returnContent;
		}
	}
	
	@Override
	public String toString() {
		return String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s", 
				this.getRequestTime(), this.getAppName(), this.getTraceId(), this.getTracePath(),
				this.getQueryData(), this.getClientIp(), this.getClientPort(), this.getServerIp(),
				this.getServerPort(), this.getUrl(), this.getElapsedTime(), this.getRequestLength(), this.getResponseLength(),
				this.getMethod(), this.getStatusCode(), this.getQueryParams(),
				this.getCallMethod(), this.getBusinessCode(), this.getReturnContent());
	}
}
