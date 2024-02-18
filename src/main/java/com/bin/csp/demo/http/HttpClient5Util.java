package com.bin.csp.demo.http;

import com.alibaba.csp.sentinel.adapter.apache.httpasyncclient.SentinelApacheHttpAsyncClientInterceptor;
import com.alibaba.csp.sentinel.adapter.apache.httpasyncclient.config.SentinelApacheHttpAsyncClientConfig;
import com.alibaba.csp.sentinel.adapter.apache.httpasyncclient.extractor.ApacheHttpAsyncClientResourceExtractor;
import com.alibaba.csp.sentinel.util.StringUtil;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.ChainElement;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClientBuilder;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 基于httpclient5的客户端
 * <p>
 * 使用时，直接创建此类的实例，指定线程数上限
 * HttpClient5Fluent client = new HttpClient5Fluent(200);
 * R r = client.put(url, headers, params, postBody, asyncCallback, connectTimeout, socketTimeout);
 * </p>
 *
 * @author BinChan
 */
public class HttpClient5Util implements Closeable {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private static final Logger logger = LoggerFactory.getLogger(HttpClient5Util.class);

    private int connectKeepAliveSecondsTimeout = 180;

    private CloseableHttpAsyncClient client;

    private static final FutureCallback<SimpleHttpResponse> EMPTY_FUTURE_CB = new FutureCallback<SimpleHttpResponse>() {
        @Override
        public void completed(SimpleHttpResponse result) {

        }

        @Override
        public void failed(Exception ex) {

        }

        @Override
        public void cancelled() {

        }
    };

    public HttpClient5Util(int maxPoolSize, String path) {
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                .setSoTimeout(Timeout.ofSeconds(3))
                .build();
        SentinelApacheHttpAsyncClientConfig config = new SentinelApacheHttpAsyncClientConfig();
        if(StringUtil.isNotBlank(path)) {
            config.setExtractor(new ApacheHttpAsyncClientResourceExtractor() {
                @Override
                public String extractor(HttpRequest httpRequest) {
                    String uri = httpRequest.getPath();
                    if (uri.startsWith(path)) {
                        uri = uri.substring(0, uri.indexOf(path) + path.length()) + "{id}";
                    }
                    return httpRequest.getMethod() + ":" + uri;
                }
            });
        }
        SentinelApacheHttpAsyncClientInterceptor interceptor = new SentinelApacheHttpAsyncClientInterceptor(config);

        client = HttpAsyncClientBuilder.create()
                .setIOReactorConfig(ioReactorConfig)
                .addExecInterceptorFirst(ChainElement.PROTOCOL.name(), interceptor)
                .setConnectionManager(PoolingAsyncClientConnectionManagerBuilder
                        .create()
                        .setMaxConnPerRoute(maxPoolSize)
                        .setMaxConnTotal(maxPoolSize)
                        .build())
                .evictIdleConnections(TimeValue.ofSeconds(2))
                .build();
        client.start();
    }

    public void setConnectKeepAliveSecondsTimeout(int connectKeepAliveSecondsTimeout) {
        this.connectKeepAliveSecondsTimeout = connectKeepAliveSecondsTimeout;
    }

    public R send(String method, String url, Map<String, String> headers, Map<String, String> params, String postBody, AsyncHttpCallback callback, int connectTimeout, int socketTimeout) {
        return call(buildRequest(method, url, headers, params, postBody, connectTimeout, socketTimeout), callback);
    }

    private SimpleHttpRequest buildRequest(String method, String url, Map<String, String> headers, Map<String, String> params, String postBody, int connectTimeout, int socketTimeout) {
        if (logger.isDebugEnabled()) {
            logger.debug("\ncall url :{}\nheaders :{}\nparams :{}\nbody :{}", url, headers, params, postBody);
        }
        //如果提交body，则务必把param参数贴到url上并进行编码，如果form表单（params）参数过长，请使用postbody方式提交
        if (params != null && !params.isEmpty()) {
            try {
                URI uri = new URIBuilder(url, StandardCharsets.UTF_8).build();
                url = uri.getScheme() + "://" + uri.getHost() + ":" + uri.getPort() + uri.getPath();
                String query = uri.getQuery();
                StringBuilder queryString = new StringBuilder(query == null ? "" : query);
                params.entrySet().forEach(entry -> queryString.append(entry.getKey()).append("=").append(entry.getValue()));
                url = url + "?" + queryString;
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            url = encodeUrl(url);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        SimpleHttpRequest request = SimpleHttpRequest.create(method, url);
        RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectTimeout(Timeout.ofMilliseconds(connectTimeout))
                .setResponseTimeout(Timeout.ofMilliseconds(socketTimeout))
                .setConnectionRequestTimeout(Timeout.ofSeconds(2))
                .setConnectionKeepAlive(TimeValue.ofSeconds(connectKeepAliveSecondsTimeout))
                .build();
        request.setConfig(requestConfig);
        Set<String> headerKeys = new HashSet<>();
        if (headers != null) {
            AtomicBoolean contains = new AtomicBoolean(false);
            headers.keySet().forEach(key -> {
                for (String k : headerKeys) {
                    if (k.equalsIgnoreCase(key)) {
                        contains.set(true);
                        break;
                    }
                }
                if (!contains.get()) {
                    headerKeys.add(key);
                }
                contains.set(false);
            });
        }
        headerKeys.forEach(key -> request.addHeader(key, headers.get(key)));

        if (postBody != null && !postBody.isEmpty()) {
            ContentType contentType = null;
            Iterator<String> keys = headerKeys.iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                if ("content-type".equalsIgnoreCase(key)) {
                    contentType = ContentType.parse(headers.get(key));
                    if (contentType.getCharset() == null) {
                        contentType = contentType.withCharset(DEFAULT_CHARSET);
                    }
                    break;
                }
            }
            if (contentType == null) {
                request.setBody(postBody, ContentType.APPLICATION_JSON);
            } else {
                request.setBody(postBody, contentType);
            }
        }
        return request;
    }

    private String encodeUrl(String url) throws UnsupportedEncodingException {
        char[] arr = url.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            char tmp = arr[i];
            if (isChinese(tmp)) {
                sb.append(URLEncoder.encode("" + tmp, "UTF-8"));
            } else {
                sb.append(tmp);
            }
        }
        return sb.toString();
    }

    private boolean isChinese(char ch) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(ch);
        if (Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS.equals(ub)
                || Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS.equals(ub)
                || Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A.equals(ub)
                || Character.UnicodeBlock.GENERAL_PUNCTUATION.equals(ub)
                || Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION.equals(ub)
                || Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS.equals(ub)) {
            return true;
        }
        return false;
    }

    private R call(SimpleHttpRequest request, AsyncHttpCallback callback) {
        if (callback == null) {
            Future<SimpleHttpResponse> future = client.execute(request, EMPTY_FUTURE_CB);
            try {
                SimpleHttpResponse response = future.get();
                response = fixContentType(response);
                R r = R.success(response.getCode(), response.getReasonPhrase());
                r.setResponseText(response.getBodyText());
                return r;
            } catch (InterruptedException e) {
                return R.failure(-1, e.getMessage());
            } catch (ExecutionException e) {
                return R.failure(-1, e.getMessage());
            }
        } else {
            client.execute(request, new FutureCallback<SimpleHttpResponse>() {
                @Override
                public void completed(SimpleHttpResponse result) {
                    result = fixContentType(result);
                    R r = R.success(result.getCode(), result.getReasonPhrase());
                    r.setResponseText(result.getBodyText());
                    callback.completed(r);
                }

                @Override
                public void failed(Exception ex) {
                    callback.failed(ex);
                }

                @Override
                public void cancelled() {
                    callback.cancelled();
                }
            });
            return R.success(200, "to access callback!");
        }

    }

    private SimpleHttpResponse fixContentType(SimpleHttpResponse response) {
        ContentType contentType = response.getContentType();
        if (logger.isDebugEnabled()) {
            logger.debug("contentType info is: {}", contentType);
        }
        if (contentType != null && contentType.getCharset() != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("need not fix content type");
            }
            return response;
        }
        if (contentType == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("content type is null");
            }
            contentType = ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), StandardCharsets.UTF_8);
        }
        contentType = ContentType
                .create(contentType.getMimeType(),
                        contentType.getCharset() == null ? StandardCharsets.UTF_8 : contentType.getCharset());
        if (logger.isDebugEnabled()) {
            logger.debug("after fixed {}", contentType);
        }
        return SimpleHttpResponse.create(response.getCode(), response.getBodyBytes(), contentType);
    }

    public R post(String url, Map<String, String> headers, Map<String, String> params, String postBody, AsyncHttpCallback callback, int connectTimeout, int socketTimeout) {
        return call(buildRequest("POST", url, headers, params, postBody, connectTimeout, socketTimeout), callback);
    }

    public R get(String url, Map<String, String> headers, Map<String, String> params, AsyncHttpCallback callback, int connectTimeout, int socketTimeout) {
        return call(buildRequest("GET", url, headers, params, null, connectTimeout, socketTimeout), callback);
    }

    public R put(String url, Map<String, String> headers, Map<String, String> params, String postBody, AsyncHttpCallback callback, int connectTimeout, int socketTimeout) {
        return call(buildRequest("PUT", url, headers, params, postBody, connectTimeout, socketTimeout), callback);
    }

    public R delete(String url, Map<String, String> headers, Map<String, String> params, String postBody, AsyncHttpCallback callback, int connectTimeout, int socketTimeout) {
        return call(buildRequest("DELETE", url, headers, params, postBody, connectTimeout, socketTimeout), callback);
    }

    public R header(String url, Map<String, String> headers, Map<String, String> params, String postBody, AsyncHttpCallback callback, int connectTimeout, int socketTimeout) {
        return call(buildRequest("HEADER", url, headers, params, postBody, connectTimeout, socketTimeout), callback);
    }

    public R patch(String url, Map<String, String> headers, Map<String, String> params, String postBody, AsyncHttpCallback callback, int connectTimeout, int socketTimeout) {
        return call(buildRequest("PATCH", url, headers, params, postBody, connectTimeout, socketTimeout), callback);
    }

    public R options(String url, Map<String, String> headers, Map<String, String> params, String postBody, AsyncHttpCallback callback, int connectTimeout, int socketTimeout) {
        return call(buildRequest("OPTIONS", url, headers, params, postBody, connectTimeout, socketTimeout), callback);
    }

    public R trace(String url, Map<String, String> headers, Map<String, String> params, String postBody, AsyncHttpCallback callback, int connectTimeout, int socketTimeout) {
        return call(buildRequest("TRACE", url, headers, params, postBody, connectTimeout, socketTimeout), callback);
    }

    @Override
    public void close() throws IOException {
        client.close();
    }
}
