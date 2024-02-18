/*
 * Copyright 1999-2019 Alibaba Group Holding Ltd.
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

import com.bin.csp.demo.vo.ResultWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Test controller
 * @author BinChan
 */
@Controller
@RequestMapping(value = "/v1/mock")
public class WebMvcTestController {
    private static final Logger logger = LoggerFactory.getLogger(WebMvcTestController.class);

    @GetMapping("/hello")
    @ResponseBody
    public ResultWrapper<String> apiHello() {
        return new ResultWrapper<String>().success("Hello");
    }

    @GetMapping("/test")
    @ResponseBody
    public ResultWrapper<String> apiTest() {
        return new ResultWrapper<String>().success();
    }

    @GetMapping("/foo")
    @ResponseBody
    public ResultWrapper<String> apiFoo() {
        doBusiness();
        return new ResultWrapper<String>().success("Foo");
    }

    @GetMapping("/foo/{id}")
    @ResponseBody
    public ResultWrapper<String> apiFoo(@PathVariable("id") String id) {
        doBusiness(id);
        return new ResultWrapper<String>().success("Foo " + id);
    }

    @GetMapping("/exclude/{id}")
    @ResponseBody
    public ResultWrapper<String> apiExclude(@PathVariable("id") String id) {
        doBusiness(id);
        return new ResultWrapper<String>().success("Exclude " + id);
    }

    @GetMapping("/forward")
    public ModelAndView apiForward() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("hello");
        return mav;
    }

    public void doBusiness(String... id) {
        Random random = new Random();
        try {
            int r = random.nextInt(300);
            logger.info("started business {} sleep {}.", id.length>0? id: "", r);
            TimeUnit.MILLISECONDS.sleep(r);
            logger.info("finished business {}.", id.length>0? id: "");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
