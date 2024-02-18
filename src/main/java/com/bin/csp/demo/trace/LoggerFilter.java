package com.bin.csp.demo.trace;

import javax.servlet.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class LoggerFilter implements Filter {
	private Set<String> skipParam = new HashSet();
	private Set<String> skipBodyPrint = new HashSet();
	private List<Pattern> skipBodyPrintPatterns = new ArrayList<>();
	/*静态资源的url，不打印返回*/
	private List<String> staticUrls = new ArrayList();
	private List<Pattern> staticUrlPatterns = new ArrayList<>();
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		ServletContext servletContext = filterConfig.getServletContext();
		if(!staticUrls.isEmpty()) {
			for(String staticUrl: staticUrls) {
				staticUrlPatterns.add(Pattern.compile(staticUrl));	
			}
		}
		if(!skipBodyPrint.isEmpty()) {
			for(String staticUrl: skipBodyPrint) {
				skipBodyPrintPatterns.add(Pattern.compile(staticUrl));	
			}
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		if(isSkip(httpRequest)) {
			chain.doFilter(request, response);
		} else {
			WrapperResponse wrapperResponse = new WrapperResponse(httpResponse);
			HttpServletRequest wrapperRequest = new WrapperRequest(httpRequest, !notPrintBody(httpRequest));
			LogTrace.startHttp(TomcatRequestWarp.get(wrapperRequest));
			LoggerService.printRequest(wrapperRequest, wrapperResponse);
			chain.doFilter(wrapperRequest, wrapperResponse);
			LoggerService.printResponse(wrapperRequest, wrapperResponse);
			LogTrace.stopHttp(TomcatResponseWrap.get(wrapperResponse));
		}
	}
	
	private boolean notPrintBody(HttpServletRequest request) {
		String url = extractUrl(request, false);
		for(Pattern p: skipBodyPrintPatterns) {
			if(p.matcher(url).matches()) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isSkip(HttpServletRequest request) {
		String url = extractUrl(request, true);
		for(Pattern p: staticUrlPatterns) {
			if(p.matcher(url).matches()) {
				return true;
			}
		}
		return false;
	}

	private String extractUrl(HttpServletRequest request, boolean includeQuery) {
		String url = request.getServletPath();
		String pathInfo = request.getPathInfo();
		String query = request.getQueryString();

		if (pathInfo != null || query != null) {
			StringBuilder sb = new StringBuilder(url);

			if (pathInfo != null) {
				sb.append(pathInfo);
			}
			if (includeQuery && query != null) {
				sb.append('?').append(query);
			}
			url = sb.toString();
		}
		return url;
	}
	
	public Set<String> getSkipParam() {
		return skipParam;
	}

	public void setSkipParam(Set<String> skipParam) {
		this.skipParam = skipParam;
	}

	public Set<String> getSkipBodyPrint() {
		return skipBodyPrint;
	}

	public void setSkipBodyPrint(Set<String> skipBodyPrint) {
		this.skipBodyPrint = skipBodyPrint;
	}

	public List<String> getStaticUrls() {
		return staticUrls;
	}

	public void setStaticUrls(List<String> staticUrls) {
		this.staticUrls = staticUrls;
	}

	@Override
	public void destroy() {
		
	}
}
