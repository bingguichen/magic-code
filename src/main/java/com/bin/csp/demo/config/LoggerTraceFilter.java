package com.bin.csp.demo.config;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.bin.csp.demo.trace.*;
import javax.servlet.*;

import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;


@Order(1)
@WebFilter(urlPatterns = "/v1/*",asyncSupported=true)
public class LoggerTraceFilter implements Filter {
    public static final String TRACE_INFO = "traceInfo";

    public final static String SESSION_ID = "sessionId";
	private Set<String> skipParam = new HashSet();
    private Set<String> skipBodyPrint = new HashSet();
    private final static List<Pattern> skipBodyPrintPatterns = new ArrayList<>();
    private List<String> staticUrls = new ArrayList();
    
    private final static List<Pattern> staticUrlPatterns = new ArrayList<>();
    private WebApplicationContext wac;
    

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext servletContext = filterConfig.getServletContext();
        wac = (WebApplicationContext) servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        if(CollectionUtil.isNotEmpty(staticUrls)) {
            for(String staticUrl: staticUrls) {
                staticUrlPatterns.add(Pattern.compile(staticUrl));
            }
        }
        if(CollectionUtil.isNotEmpty(skipBodyPrint)) {
            for(String staticUrl: skipBodyPrint) {
                skipBodyPrintPatterns.add(Pattern.compile(staticUrl));
            }
        }
       
        String appName = wac.getEnvironment().getProperty("spring.application.name");
        LogTrace.init(appName, LoggerFactory.getLogger("logTraceAppender"));
    }
    @Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
        String contentType = httpRequest.getContentType();
        if (contentType != null && contentType.contains("application/x-www-form-urlencoded")) {
            //如果是application/x-www-form-urlencoded, 参数值在request body中以 a=1&b=2&c=3...形式存在，
            //若直接构造BodyReaderHttpServletRequestWrapper，在将流读取并存到copy字节数组里之后,
            //httpRequest.getParameterMap()将返回空值！
            //若运行一下 httpRequest.getParameterMap(), body中的流将为空! 所以两者是互斥的！
            httpRequest.getParameterMap();
        }

        MDC.put(SESSION_ID, RandomUtil.randomString(32));
        WrapperResponse wrapperResponse = new WrapperResponse(httpResponse);
        HttpServletRequest wrapperRequest = new WrapperRequest(httpRequest, !notPrintBody(httpRequest));
        LogTrace.startHttp(TomcatRequestWarp.get(wrapperRequest));
        LoggerService.printRequest(wrapperRequest, wrapperResponse);
        MDC.put(TraceConstant.DEFAULT_TRACE_REQId, LogTrace.getData(TraceConstant.DEFAULT_TRACE_REQId));
        MDC.put(TRACE_INFO, JSONUtil.toJsonStr(LogTrace.get()));

        chain.doFilter(wrapperRequest, wrapperResponse);
        LoggerService.printResponse(wrapperRequest, wrapperResponse);
        LogTrace.stopHttp(TomcatResponseWrap.get(wrapperResponse));
        MDC.clear();
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
}
