package com.bin.csp.demo.trace;


import javax.servlet.http.HttpServletRequest;

public class TomcatRequestWarp implements IHttpRequest {
	private HttpServletRequest request;
	private TomcatRequestWarp(HttpServletRequest request) {
		this.request = request;
	}
	@Override
	public String localHost() {
		return request.getLocalAddr();
	}
	@Override
	public int localPort() {
		return request.getLocalPort();
	}
	@Override
	public String remoteHost() {
		return request.getRemoteAddr();
	}
	@Override
	public int remotePort() {
		return request.getRemotePort();
	}
	@Override
	public String method() {
		return request.getMethod();
	}
	@Override
	public int contentLength() {
		return request.getContentLength();
	}
	@Override
	public String getHeader(String name) {
		return request.getHeader(name);
	}
	@Override
	public String path() {
		return request.getRequestURI();
	}
	@Override
	public String query() {
		return request.getQueryString();
	}
	
	public static IHttpRequest get(HttpServletRequest request) {
		return new TomcatRequestWarp(request);
	}
	
}
