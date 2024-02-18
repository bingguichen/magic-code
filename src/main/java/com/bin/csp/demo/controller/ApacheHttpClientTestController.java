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

import com.alibaba.csp.sentinel.adapter.apache.httpclient.SentinelApacheHttpClientBuilder;
import com.alibaba.csp.sentinel.adapter.apache.httpclient.config.SentinelApacheHttpClientConfig;
import com.alibaba.csp.sentinel.adapter.apache.httpclient.extractor.ApacheHttpClientResourceExtractor;
import com.bin.csp.demo.vo.ResultWrapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author BinChan
 */
@RestController
@RequestMapping(value = "/v1/apache/httpclient/sync")
public class ApacheHttpClientTestController {
    private static final Logger logger = LoggerFactory.getLogger(ApacheHttpClientTestController.class);

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
        return new ResultWrapper<String>().success("Welcome Sync Back! " + new Date());
    }

    @RequestMapping("/back/{id}")
    public ResultWrapper<String> back(@PathVariable String id) {
        doBusiness();
        return new ResultWrapper<String>().success("Welcome Sync Back! " + id + " " + new Date());
    }

    @RequestMapping("/hello")
    public ResultWrapper<String> sync() throws Exception {
        SentinelApacheHttpClientConfig config = new SentinelApacheHttpClientConfig();
        config.setExtractor(new ApacheHttpClientResourceExtractor() {

            @Override
            public String extractor(HttpRequestWrapper request) {
                String contains = "/v1/apache/httpclient/sync/back/";
                String uri = request.getRequestLine().getUri();
                if (uri.startsWith(contains)) {
                    uri = uri.substring(0, uri.indexOf(contains) + contains.length()) + "{id}";
                }
                return request.getMethod() + ":" + uri;
            }
        });
        CloseableHttpClient httpclient = new SentinelApacheHttpClientBuilder(config).build();

        HttpGet httpGet = new HttpGet("http://localhost:" + port + "/v1/apache/httpclient/sync/back");
        return new ResultWrapper<String>().success(getRemoteString(httpclient, httpGet));
    }

    @RequestMapping("/hello/{id}")
    public ResultWrapper<String> sync(@PathVariable String id) throws Exception {
        SentinelApacheHttpClientConfig config = new SentinelApacheHttpClientConfig();
        config.setExtractor(new ApacheHttpClientResourceExtractor() {

            @Override
            public String extractor(HttpRequestWrapper request) {
                String contains = "/v1/apache/httpclient/sync/back/";
                String uri = request.getRequestLine().getUri();
                if (uri.startsWith(contains)) {
                    uri = uri.substring(0, uri.indexOf(contains) + contains.length()) + "{id}";
                }
                return request.getMethod() + ":" + uri;
            }
        });
        CloseableHttpClient httpclient = new SentinelApacheHttpClientBuilder(config).build();

        HttpGet httpGet = new HttpGet("http://localhost:" + port + "/v1/apache/httpclient/sync/back/" + id);
        return new ResultWrapper<String>().success(getRemoteString(httpclient, httpGet));
    }

    private String getRemoteString(CloseableHttpClient httpclient, HttpGet httpGet) throws IOException {
        String result;
        HttpContext context = new BasicHttpContext();
        CloseableHttpResponse response;
        response = httpclient.execute(httpGet, context);
        try {
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }
        httpclient.close();
        return result;
    }


    @RequestMapping("/mock/err")
    public ResultWrapper<String> testErr() throws Exception {
        logger.debug("mock err...");
        CloseableHttpClient httpclient = new SentinelApacheHttpClientBuilder().build();

        HttpGet httpGet = new HttpGet("http://127.0.0.1:10001/v1/mock/err");
        return new ResultWrapper<String>().success(getRemoteString(httpclient, httpGet));
    }
}
