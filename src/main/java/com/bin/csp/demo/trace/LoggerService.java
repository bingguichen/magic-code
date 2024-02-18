package com.bin.csp.demo.trace;

import cn.hutool.core.date.SystemClock;
import com.bin.csp.demo.json.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author BinChan
 */
public class LoggerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerService.class);

    private static final String LOGVO_KEY = "msart.request.logVo";

    private static final Set<String> SKIP_PARAMS = new HashSet<>(Collections.singletonList("pic"));
    private final static Pattern CRLF = Pattern.compile("(\r\n|\r|\n|\n\r|\\s+|\n\t)");


    public static LoggerVo getParameter(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        LoggerVo loggerVo = new LoggerVo();
        loggerVo.setUrl(httpRequest.getRequestURL().toString());
        Map<String, String> params = new HashMap<>();
        @SuppressWarnings("rawtypes")
        Enumeration enu = httpRequest.getParameterNames();
        while (enu.hasMoreElements()) {
            String paramName = (String) enu.nextElement();
            if (SKIP_PARAMS.contains(paramName)) {
                continue;
            }
            String paramValue = httpRequest.getParameter(paramName);
            params.put(paramName, paramValue);
            // 屏蔽密码
            if ("password".equalsIgnoreCase(paramName)) {
                params.put(paramName, "*****");
            }
        }
        WrapperRequest wrapperRequest = (WrapperRequest) httpRequest;
        if (wrapperRequest.isPrintBody()) {
            try {
                if (wrapperRequest.getInput() != null) {
                    String msg = CRLF.matcher(new String(wrapperRequest.getInput(), StandardCharsets.UTF_8))
                            .replaceAll("");
                    if (JsonUtil.isJsonObj(msg)) {
                        Map<String, Object> node = JsonUtil.parseMap(msg, String.class, Object.class);
                        if (node.containsKey(TraceConstant.DEFAULT_TRACE_REQId)) {
                            loggerVo.setReqId(String.valueOf(node.get(TraceConstant.DEFAULT_TRACE_REQId)));
                        }
                    }
                    params.put("body", msg);
                }
				if (Objects.isNull(loggerVo.getReqId())) {
					String reqId = wrapperRequest.getParameter(TraceConstant.DEFAULT_TRACE_REQId);
					if (Objects.isNull(reqId)) {
						reqId = wrapperRequest.getHeader(TraceConstant.DEFAULT_TRACE_REQId);
					}
					loggerVo.setReqId(reqId);
				}
            } catch (IOException e) {
                LOGGER.error("读取request输入流异常", e);
            }
        }
        loggerVo.setParams(params);
        // nginx反向代理，尝试获取真实ip地址 //需要nginx中配置
        String remoteAddr = httpRequest.getRemoteAddr();
        String forwarded = httpRequest.getHeader("X-Forwarded-For");
        String realIp = httpRequest.getHeader("X-Real-IP");
        String ip = "";
        if (realIp == null) {
            if (forwarded == null) {
                ip = remoteAddr;
            } else {
                int index = forwarded.indexOf(",");
                if (index != -1) {
                    ip = forwarded.substring(0, index);
                }
            }
        } else {
            ip = realIp;
        }
        loggerVo.setIp(ip);
        return loggerVo;
    }

    public static void printRequest(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {

        LoggerVo loggerVo;
        try {
            loggerVo = getParameter(httpRequest, httpResponse);
            loggerVo.setStart(SystemClock.now());
            httpRequest.setAttribute(LOGVO_KEY, loggerVo);
            // 最终没有获取到reqId，则从请求头中获取
            if (loggerVo.getReqId() == null || loggerVo.getReqId().isEmpty()) {
                loggerVo.setReqId(httpRequest.getHeader(TraceConstant.DEFAULT_TRACE_REQId));
            }
            LogTrace.putData("reqId", loggerVo.getReqId());
            LOGGER.info(">>>ReceiveHttpReq:reqId=" + loggerVo.getReqId() + " sid=" + LogTrace.getTraceId() +
                    "method= " + httpRequest.getMethod() +
                    " " + loggerVo.toString());
        } catch (Exception e) {
            LOGGER.error("request print Exception=" + e.getMessage(), e);
        }

    }

    public static void printResponse(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        try {
            LoggerVo loggerVo = (LoggerVo) httpRequest.getAttribute(LOGVO_KEY);
            if(null == loggerVo){
                return;
            }
            WrapperResponse wrapperResponse = (WrapperResponse) httpResponse;
            byte[] data = wrapperResponse.getResponseData();
            String result = new String(data);
            loggerVo.setExecuteTime(System.currentTimeMillis() - loggerVo.getStart());
            loggerVo.setResult(result);
            LOGGER.info("<<<CompleteHttpReq:reqId=" + loggerVo.getReqId() + " sid=" + LogTrace.getTraceId() +
                    " " + loggerVo.toString());
        } catch (Exception e) {
            LOGGER.error("reponse print Exception=" + e.getMessage(), e);
        }
    }
}
