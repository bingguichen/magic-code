package com.bin.csp.demo.trace;

public interface IHttpRequest {
	String localHost();
	int localPort();
	String remoteHost();
	int remotePort();
	String method();
	int contentLength();
	String getHeader(String name);
	String path();
	String query();
}
