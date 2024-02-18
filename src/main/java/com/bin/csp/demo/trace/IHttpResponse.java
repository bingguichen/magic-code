package com.bin.csp.demo.trace;

public interface IHttpResponse {
	String content();
	long contentLength();
	int statusCode();
	String getHeader(String name);
}
