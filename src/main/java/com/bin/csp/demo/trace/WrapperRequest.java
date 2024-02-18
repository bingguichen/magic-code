package com.bin.csp.demo.trace;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/** 
 *  WrapperRequest
 * ServletRequest包装类，主要用于打印请求参数
 * @author BinChan
 *  
 */
public class WrapperRequest extends HttpServletRequestWrapper {
	private byte[] requestBody = null;
	private boolean printBody = true;

	public WrapperRequest(HttpServletRequest request) {
		super(request);
		try {
			InputStream in = request.getInputStream();
			if (in == null) {
				this.requestBody = new byte[0];
			} else {
				ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
				byte[] buffer = new byte[1024];

				int bytesRead;
				while((bytesRead = in.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
				}

				out.flush();
				this.requestBody = out.toByteArray();
			}
		} catch (IOException var6) {
			var6.printStackTrace();
		}

	}

	public WrapperRequest(HttpServletRequest request, boolean printBody) {
		super(request);
		this.printBody = printBody;
		if (printBody) {
			try {
				InputStream in = request.getInputStream();
				if (in == null) {
					this.requestBody = new byte[0];
				} else {
					ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
					byte[] buffer = new byte[1024];

					int bytesRead;
					while((bytesRead = in.read(buffer)) != -1) {
						out.write(buffer, 0, bytesRead);
					}

					out.flush();
					this.requestBody = out.toByteArray();
				}
			} catch (IOException var7) {
				var7.printStackTrace();
			}
		}

	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return printBody ?
				new SmartInputStream(new ByteArrayInputStream(requestBody)) : super.getInputStream();
	}

	public byte[] getInput() throws IOException {
		return this.requestBody;
	}

	public boolean isPrintBody() {
		return this.printBody;
	}

	public void setRequestBody(byte[] requestBody) {
		this.printBody = true;
		this.requestBody = requestBody;
	}

	public static class SmartInputStream extends ServletInputStream {
		ByteArrayInputStream bas;

		SmartInputStream(ByteArrayInputStream bas) {
			this.bas = bas;
		}

		@Override
		public boolean isFinished() {
			return false;
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setReadListener(ReadListener listener) {
		}

		@Override
		public int read() throws IOException {
			return this.bas.read();
		}
	}
}
