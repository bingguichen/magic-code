/*
 * Copyright 1999-2020 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bin.csp.demo.controller;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.csp.sentinel.adapter.apache.httpasyncclient.SentinelApacheHttpAsyncClientInterceptor;
import com.alibaba.csp.sentinel.adapter.apache.httpasyncclient.config.SentinelApacheHttpAsyncClientConfig;
import com.alibaba.csp.sentinel.adapter.apache.httpasyncclient.extractor.ApacheHttpAsyncClientResourceExtractor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.bin.csp.demo.http.HttpClient5Util;
import com.bin.csp.demo.http.JsonUtilPlus;
import com.bin.csp.demo.http.R;
import com.bin.csp.demo.json.JsonUtil;
import com.bin.csp.demo.vo.ResultWrapper;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder;
import org.apache.hc.client5.http.impl.ChainElement;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.message.StatusLine;
import org.apache.hc.core5.io.CloseMode;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author BinChan
 */
@RestController
@RequestMapping(value = "/v1/apache/httpclient/async")
public class ApacheHttpAsyncClientTestController {
    private static final Logger logger = LoggerFactory.getLogger(ApacheHttpAsyncClientTestController.class);

    @Value("${server.port}")
    private Integer port;

    public void doBusiness() {
        Random random = new Random();
        try {
            int r = random.nextInt(300);
            logger.info("started doing business {}.", r);
            TimeUnit.MILLISECONDS.sleep(r);
            logger.info("finished done business.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping("/back")
    public ResultWrapper<String> back() {
        doBusiness();
        return new ResultWrapper<String>().success("Welcome Async Back! " + new Date());
    }

    @RequestMapping("/back/{id}")
    public ResultWrapper<String> back(@PathVariable String id) {
        doBusiness();
        return new ResultWrapper<String>().success("Welcome Async Back! " + id + " " + new Date());
    }

    @RequestMapping("/hello")
    public ResultWrapper<String> sync() throws Exception {
        HttpClient5Util client = new HttpClient5Util(100, "/v1/mock/foo/");
        R r = client.get("http://127.0.0.1:10001/v1/mock/foo", null, null, null, 1000, 3000);
        client.close();
        if (r == null ) {
            return new ResultWrapper<String>().exception();
        } else if(r.getStatusCode() != HttpStatus.OK.value()){
            logger.error("call service fail, r={}", JsonUtil.toJson(r));
            return new ResultWrapper<String>().exception(String.valueOf(r.getStatusCode()), r.getStatusMsg());
        } else if(StringUtils.isEmpty(r.getResponseText())){
            logger.warn("call service success but content empty, r={}", JsonUtil.toJson(r));
            return new ResultWrapper<String>().success();
        } else {
            return JsonUtilPlus.parse(r.getResponseText(), new TypeReference<ResultWrapper<String>>() {});
        }
    }



    @RequestMapping("/hello/{id}")
    public ResultWrapper<String> sync(@PathVariable String id) throws Exception {
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                .setSoTimeout(Timeout.ofSeconds(5))
                .build();
        SentinelApacheHttpAsyncClientConfig config = new SentinelApacheHttpAsyncClientConfig();
        config.setExtractor(new ApacheHttpAsyncClientResourceExtractor() {

            @Override
            public String extractor(HttpRequest httpRequest) {
                String contains = "/v1/mock/foo/";
                String uri = httpRequest.getPath();
                if (uri.startsWith(contains)) {
                    uri = uri.substring(0, uri.indexOf(contains) + contains.length()) + "{id}";
                }
                return httpRequest.getMethod() + ":" + uri;
            }
        });
        SentinelApacheHttpAsyncClientInterceptor interceptor = new SentinelApacheHttpAsyncClientInterceptor(config);
        CloseableHttpAsyncClient client = HttpAsyncClients.custom()
                .setIOReactorConfig(ioReactorConfig)
                .addExecInterceptorFirst(ChainElement.PROTOCOL.name(), interceptor)
                .build();

        client.start();
        final String requestUri = "http://127.0.0.1:10001/v1/mock/foo/" + id;
        SimpleHttpRequest request = SimpleRequestBuilder.get(requestUri).build();
        Future<SimpleHttpResponse> future = client.execute(
                request,
                new FutureCallback<SimpleHttpResponse>() {

                    @Override
                    public void completed(final SimpleHttpResponse response) {
                        logger.info(request + "->" + new StatusLine(response));
                        logger.info(response.getBody().toString());
                    }

                    @Override
                    public void failed(final Exception ex) {
                        logger.info(request + "->" + ex);
                    }

                    @Override
                    public void cancelled() {
                        logger.info(request + " cancelled");
                    }

                });
        SimpleHttpResponse response = future.get();
        client.close(CloseMode.GRACEFUL);
        return JsonUtilPlus.parse(response.getBodyText(), new TypeReference<ResultWrapper<String>>() {});
    }


    @RequestMapping("/mock/foo")
    public ResultWrapper<String> testErr() throws Exception {
        logger.debug("mock err...");
        HttpClient5Util client = new HttpClient5Util(100, "/v1/mock/foo/");
        R r = client.get("http://127.0.0.1:10001/v1/mock/foo", null, null, null, 1000, 3000);
        client.close();
        if (r == null ) {
            return new ResultWrapper<String>().exception();
        } else if(r.getStatusCode() != HttpStatus.OK.value()){
            logger.error("call service fail, r={}", JsonUtil.toJson(r));
            return new ResultWrapper<String>().exception(String.valueOf(r.getStatusCode()), r.getStatusMsg());
        } else if(StringUtils.isEmpty(r.getResponseText())){
            logger.warn("call service success but content empty, r={}", JsonUtil.toJson(r));
            return new ResultWrapper<String>().success();
        } else {
            return JsonUtilPlus.parse(r.getResponseText(), new TypeReference<ResultWrapper<String>>() {});
        }
    }
}
