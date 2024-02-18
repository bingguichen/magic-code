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

import com.alibaba.csp.sentinel.adapter.okhttp.SentinelOkHttpConfig;
import com.alibaba.csp.sentinel.adapter.okhttp.SentinelOkHttpInterceptor;
import com.alibaba.csp.sentinel.adapter.okhttp.fallback.DefaultOkHttpFallback;
import com.bin.csp.demo.vo.ResultWrapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
@RequestMapping(value = "/v1/okhttp")
public class OkHttpTestController {
    private static final Logger logger = LoggerFactory.getLogger(OkHttpTestController.class);

    @Value("${server.port}")
    private Integer port;

    private final OkHttpClient client = new OkHttpClient.Builder()
        .addInterceptor(new SentinelOkHttpInterceptor(new SentinelOkHttpConfig((request, connection) -> {
            String regex = "/v1/okhttp/back/";
            String url = request.url().uri().getPath();
            if (url.contains(regex)) {
                url = url.substring(0, url.indexOf(regex) + regex.length()) + "{id}";
            }
            return request.method() + ":" + url;
        }, new DefaultOkHttpFallback())))
        .build();

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
        return new ResultWrapper<String>().success("Welcome OK Back! " + new Date());
    }

    @RequestMapping("/back/{id}")
    public ResultWrapper<String> back(@PathVariable String id) {
        doBusiness();
        return new ResultWrapper<String>().success("Welcome OK Back! " + id + " " + new Date());
    }

    @RequestMapping("/testcase/{id}")
    public ResultWrapper<String> testcase(@PathVariable String id) throws Exception {
        logger.debug("testcase {}...", id);
        return new ResultWrapper<String>().success(getRemoteString(id));
    }

    @RequestMapping("/testcase")
    public ResultWrapper<String> testcase() throws Exception {
        logger.debug("testcase...");
        return new ResultWrapper<String>().success(getRemoteString(null));
    }

    private String getRemoteString(String id) throws IOException {
        Request request = new Request.Builder()
            .url("http://127.0.0.1:" + port + "/v1/okhttp/back" + (id == null ? "" : "/" + id))
            .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    @RequestMapping("/testerr")
    public ResultWrapper<String> testErr() throws Exception {
        logger.debug("testErr...");
        Request request = new Request.Builder()
                .url("http://127.0.0.1:10001/v1/err")
                .build();
        Response response = client.newCall(request).execute();
        return new ResultWrapper<String>().success(response.body().string());
    }
}