package com.bin.csp.demo.trace;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

/**
 * 自定义一个响应结果包装器，将在这里提供一个基于内存的输出器来存储所有 返回给客户端的内容，包括OutputStream或者PrintWriter。
 *
 *
 */
public class WrapperResponse extends HttpServletResponseWrapper {
	private ByteArrayOutputStream buffer = null;
	private ServletOutputStream out = null;
	private PrintWriter writer = null;
	private boolean isPrintWriter=true;
	
	public WrapperResponse(HttpServletResponse resp) throws IOException {
		super(resp);
		buffer = new ByteArrayOutputStream();// 真正存储数据的流
		writer = new PrintWriter(new OutputStreamWriter(buffer,
				this.getCharacterEncoding()));
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if(out == null) {
			out = new WapperedOutputStream(super.getOutputStream());
		}
		return out;
	}

	@Override
	public PrintWriter getWriter() throws UnsupportedEncodingException {
		return writer;
	}

	
	public boolean isPrintWriter(){
		return isPrintWriter;
	}
	
	@Override
	public void reset() {
		buffer.reset();
	}

	public byte[] getResponseData() throws IOException {
		return ((WapperedOutputStream)getOutputStream()).getOutPut();
	}

	class WapperedOutputStream extends ServletOutputStream {
		private ByteArrayOutputStream bas = null;
		private final ServletOutputStream delegated;
		public WapperedOutputStream(ServletOutputStream sos)
				throws IOException {
			bas = new ByteArrayOutputStream();
			delegated = sos;
		}

		@Override
		public void write(int b) throws IOException {
			isPrintWriter=false;
			bas.write(b);
			delegated.write(b);
		}

		@Override
		public void write(byte[] b) throws IOException {
			isPrintWriter=false;
			bas.write(b, 0, b.length);
			delegated.write(b);
		}

		@Override
		public boolean isReady() {
			return false;
		}

		@Override
		public void setWriteListener(WriteListener listener) {
			
		}
		
		public byte[] getOutPut() {
			return bas.toByteArray();
		}
	}

}
