package com.bin.csp.demo.trace;

import java.io.IOException;

public class TomcatResponseWrap implements IHttpResponse {

	private WrapperResponse response;
	
	private TomcatResponseWrap(WrapperResponse response) {
		this.response = response;
	}
	
	@Override
	public String content() {
		try {
			return new String(response.getResponseData());
		} catch (IOException e) {
			return "";
		}
	}

	@Override
	public long contentLength() {
		return response.getBufferSize();
	}

	@Override
	public int statusCode() {
		return response.getStatus();
	}

	@Override
	public String getHeader(String name) {
		return response.getHeader(name);
	}

	public static IHttpResponse get(WrapperResponse response) {
		return new TomcatResponseWrap(response);
	}
}
