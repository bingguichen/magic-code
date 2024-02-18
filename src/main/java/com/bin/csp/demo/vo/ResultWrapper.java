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
package com.bin.csp.demo.vo;

import com.alibaba.fastjson.JSONObject;

/**
 * @author BinChan
 */
public class ResultWrapper<T> {

    private String code;
    private String message;
    private T data;


    public ResultWrapper(String code, String message) {
        this.code = code;
        this.message = message;
    }
    public ResultWrapper(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResultWrapper() {

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResultWrapper<T> blocked() {
        return new ResultWrapper<>("-1", "Blocked by Sentinel");
    }

    public ResultWrapper<T> exception() {
        return new ResultWrapper<>("1", "Exception");
    }

    public ResultWrapper<T> exception(String code, String message) {
        return new ResultWrapper<>(code, message);
    }

    public ResultWrapper<T> success() {
        return new ResultWrapper<>("0", "Success");
    }

    public ResultWrapper<T> success(T data) {
        return new ResultWrapper<>("0", "Success", data);
    }
    public String toJsonString() {
        return JSONObject.toJSONString(this);
    }
}
