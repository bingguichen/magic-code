package com.bin.csp.demo.http;

import com.alibaba.csp.sentinel.slots.block.SentinelRpcException;
import com.bin.csp.demo.json.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * TODO 补充注释
 *
 * @author BinChan
 */
public class HttpClient5UtilPerf {
    private static final Logger logger = LoggerFactory.getLogger(HttpClient5UtilPerf.class);
    /**
     * {
     * "method": "get",
     * "url": "http://127.0.0.1:8080/get/123",
     * "body": null,
     * "header":["Content-Type: application/json; charset=UTF-8"],
     * "count": 100000,
     * "concurrent": 200,
     * "timeout": 3000
     * }
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        String content = "{\"reqMethod\":\"GET\",\"url\": \"http://127.0.0.1:10001/v1/mock/foo/{id}\",\"body\": null," +
                "\"headers\":[\"Content-Type: application/json; charset=UTF-8\"]," +
                "\"count\": 5,\"concurrent\": 3,\"timeout\": 5000}";

        HttpClient5UtilPerfBean bean = JsonUtil.parse(content.toString(), HttpClient5UtilPerfBean.class);
        HttpClient5Util client = new HttpClient5Util(bean.getConcurrent(), "/v1/mock/foo/");
        Map<String, String> headers = Arrays.stream(bean.getHeaders()).map(item -> item.split(":")).collect(Collectors.toMap(v -> v[0], v -> v[1]));
        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger failed = new AtomicInteger(0);
        AtomicInteger totalTask = new AtomicInteger(0);
        //采用异步方式统计
        CountDownLatch latch = new CountDownLatch(bean.getCount());
        long start = System.currentTimeMillis();
        for (int i = 0; i < bean.getCount(); i++) {
            int finalI = i;
            try {
                client.send(bean.getReqMethod(),
                        bean.getUrl().replace("{id}", String.valueOf(i)), headers, null, bean.getBody(),
                        new AsyncHttpCallback("" + finalI) {
                            @Override
                            public void failed(Exception e) {
                                logger.error(finalI + "failed : " + e.getMessage());
                                failed.incrementAndGet();
                                totalTask.decrementAndGet();
                                latch.countDown();
                            }

                            @Override
                            public void completed(R r) {
                                success.incrementAndGet();
                                totalTask.decrementAndGet();
                                latch.countDown();
                            }

                            @Override
                            public void cancelled() {
                                failed.incrementAndGet();
                                totalTask.decrementAndGet();
                                latch.countDown();
                            }
                        }, bean.getTimeout(), bean.getTimeout());

            } catch (SentinelRpcException ex) {
                logger.error(finalI + "failed : " + ex.getMessage());
            } finally {
                totalTask.incrementAndGet();
            }
        }
        latch.await();
        long end = System.currentTimeMillis();
        logger.info("total cost ms: " + (end - start));
        logger.info("success : " + success.get());
        logger.info("failed : " + failed.get());
        client.close();
    }
}
