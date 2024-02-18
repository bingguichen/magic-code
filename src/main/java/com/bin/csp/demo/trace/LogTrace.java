package com.bin.csp.demo.trace;

import cn.hutool.core.date.SystemClock;
import com.bin.csp.demo.util.IpUtil;
import org.slf4j.Logger;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author BinChan
 */
public class LogTrace {

    private static final ThreadLocal<LogTraceEntity> logVo = new ThreadLocal<>();

    private static String logAppName = "default-trace-app";
    private static Logger logger;
    static {
        String tmp = System.getProperty("java.class.path");
        if (tmp != null && !tmp.isEmpty()) {
            String[] ary = tmp.split("/");
            logAppName = ary[ary.length - 1];
        }
        serverIp = IpUtil.getRealLocalIP()[0];
    }

    private static String serverIp;

    public static void init(String appName, Logger logObj) {
        logAppName = appName;
        logger = logObj;
    }

    public static void startHttp(IHttpRequest request) {
        //String serverIp = request.localHost();
        int serverPort = request.localPort();
        LogTraceEntity log = new LogTraceEntity();
        log.setRequestTime(SystemClock.now());
        log.setAppName(logAppName);
        log.setClientIp(getClientIp(request));
        log.setClientPort(request.remotePort());
        log.setServerIp(serverIp);
        log.setServerPort(serverPort);
        log.setMethod(request.method());
        long requestLen = request.contentLength();
        log.setRequestLength(requestLen);

        String traceId = request.getHeader(TraceConstant.DEFAULT_TRACE_ID);
        String tracePath = request.getHeader(TraceConstant.DEFAULT_TRACE_PATH);
        if (traceId == null || traceId.isEmpty()) {
            traceId = UUID.randomUUID().toString().replace("-", "");
            tracePath = "";
        } else {
            tracePath += "$";
        }
        tracePath = String.format("%s%s:%s:%s:%s", tracePath, System.currentTimeMillis(), logAppName, serverIp,
                serverPort);

        log.setTraceId(traceId);
        log.setTracePath(tracePath);
        log.setUrl(request.path());
        log.setQueryParams(request.query());
        logVo.set(log);
    }

    public static void stopHttp(IHttpResponse response) {
        LogTraceEntity log = logVo.get();
        if (log == null) {
            return;
        }
        log.setElapsedTime(SystemClock.now() - log.getRequestTime());
        log.setStatusCode(response.statusCode());
        log.setResponseLength(response.contentLength());
        String content = response.content();
        if (content != null && !content.isEmpty()) {
            log.setBusinessCode(getCode(content));
            log.setReturnContent(content);
        }
        if (logger != null) {
            logger.info(log.toString());
        }
        logVo.remove();
    }

    private static String getClientIp(IHttpRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        ip = getIpFromHeaderIfNotAlreadySet("X-Real-IP", request, ip);
        ip = getIpFromHeaderIfNotAlreadySet("Proxy-Client-IP", request, ip);
        ip = getIpFromHeaderIfNotAlreadySet("WL-Proxy-Client-IP", request, ip);
        ip = getIpFromHeaderIfNotAlreadySet("HTTP_CLIENT_IP", request, ip);
        ip = getIpFromHeaderIfNotAlreadySet("HTTP_X_FORWARDED_FOR", request, ip);
        ip = getFirstIp(ip);
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) || "127.0.0.1".equalsIgnoreCase(ip)) {
            ip = request.remoteHost();
        }
        return ip;
    }

    private static String getFirstIp(String ip) {
        if (ip != null) {
            final int indexOfFirstComma = ip.indexOf(',');
            if (indexOfFirstComma != -1) {
                ip = ip.substring(0, indexOfFirstComma);
            }
        }
        return ip;
    }

    private static String getIpFromHeaderIfNotAlreadySet(String header, IHttpRequest request, String ip) {
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader(header);
        }
        return ip;
    }

    public static void startRpc(String serverIp, int serverPort, String remoteAddr) {
        LogTraceEntity log = new LogTraceEntity();
        log.setRequestTime(System.currentTimeMillis());
        log.setAppName(logAppName);
        String[] ary = (remoteAddr == null || remoteAddr.isEmpty()) ? "".split(":") : remoteAddr.split(":");
        log.setClientIp(ary[0]);
        try {
            if (ary.length > 1) {
                log.setClientPort(Integer.parseInt(ary[1]));
            } else {
                log.setClientPort(0);
            }
        } catch (Exception ignored) {
        }
        log.setServerIp(serverIp);
        log.setServerPort(serverPort);
        logVo.set(log);
    }

    public static void updateRpc(String traceId, String tracePath, String method, long requestLen) {
        LogTraceEntity log = logVo.get();
        log.setMethod(method);
        log.setRequestLength(requestLen);
        if (traceId == null || traceId.isEmpty()) {
            traceId = UUID.randomUUID().toString().replace("-", "");
            tracePath = "";
        } else {
            tracePath += "$";
        }
        tracePath = String.format("%s%s:%s:%s:%s", tracePath, SystemClock.now(), logAppName, log.getServerIp(),
                log.getServerPort());

        log.setTraceId(traceId);
        log.setTracePath(tracePath);
        log.setUrl(method);
    }

    public static void stopRpc(String callMethod, int statusCode, long respLength, String content) {
        LogTraceEntity log = logVo.get();
        if (log == null) {
            return;
        }

        if (callMethod != null && !callMethod.isEmpty()) {
            log.setCallMethod(callMethod);
        }

        log.setElapsedTime(System.currentTimeMillis() - log.getRequestTime());
        log.setStatusCode(statusCode);
        // 从content中提取内容和businessCode
        if (content != null && !content.isEmpty()) {
			log.setBusinessCode(getCode(content));
            log.setReturnContent(content);
        }
        if (log.getBusinessCode() == null || log.getBusinessCode().isEmpty()) {
            log.setBusinessCode(String.valueOf(statusCode));
        }
        log.setResponseLength(respLength);

        if (logger != null) {
            logger.info(log.toString());
        }
        logVo.remove();
    }

    public static LogTraceEntity get() {
        return logVo.get();
    }

    public static void putData(String key, String val) {
        LogTraceEntity log = logVo.get();
        if (log != null && key != null && !key.isEmpty() && val != null && !val.isEmpty()) {
            log.pubQueryData(key, val);
        }
    }

    public static String getData(String key) {
        LogTraceEntity log = logVo.get();
        if (log != null) {
            return log.getQueryData(key);
        }
        return "";
    }

    public static void setCallMethod(String callMethod) {
        LogTraceEntity log = logVo.get();
        if (log != null) {
            log.setCallMethod(callMethod);
        }
    }

    public static String getTraceId() {
        LogTraceEntity log = logVo.get();
        if (log != null) {
            return log.getTraceId();
        } else {
            return "";
        }
    }

    public static String getTracePath() {
        LogTraceEntity log = logVo.get();
        if (log != null) {
            return log.getTracePath();
        } else {
            return "";
        }
    }

    public static void setBusinessCode(String businessCode) {
        LogTraceEntity log = logVo.get();
        if (log != null) {
            log.setBusinessCode(businessCode);
        }
    }

    private static final Pattern pattern = Pattern.compile("\"code\"\\s*\\:\\s*\"?\\d+\"?");

    public static String getCode(String content) {
        if (content == null || content.isEmpty()) {
            return "";
        }
        Matcher m = pattern.matcher(content);
        if (m.find()) {
            String g = m.group();
			String[] items = g.replace(" ", "").split(":");
            return items[1].replace("\"", "");
        } else {
            return "";
        }
    }
}
