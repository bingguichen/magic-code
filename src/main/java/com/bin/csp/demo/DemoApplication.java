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
package com.bin.csp.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * <p>Add the JVM parameter to connect to the dashboard:</p>
 * {@code -javaagent:D:\Tools\otel\opentelemetry-javaagent.jar -Dotel.javaagent.configuration-file=D:\Tools\otel\opentelemetry-javaagent.properties -Dotel.service.name=demo-10001 -Dproject.name=demo-10001}
 * {@code --spring.config.location=D:\Workspace\OpenSource\demo\target\conf\magic-code.yml}
 * @author BinChan
 */
@SpringBootApplication
public class DemoApplication {
    private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
