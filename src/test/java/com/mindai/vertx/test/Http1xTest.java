package com.mindai.vertx.test;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestOptions;
import io.vertx.ext.unit.TestSuite;
import io.vertx.ext.unit.report.ReportOptions;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

public class Http1xTest {
    public static AtomicLong AL = new AtomicLong(0);

    Vertx vertx;

    @Before
    public void init() {
        if (vertx == null) {
            vertx = Vertx.vertx();
        }
    }

    @Test
    public void testNormal() {
        TestSuite suite = TestSuite.create("the_test_suite");

        /*suite.before(testContext -> {
            HttpServer server = vertx.createHttpServer();
            server.requestHandler(httpServerRequest -> {
                httpServerRequest.response().end("hello,world!");
            });
            server.listen(8080, httpServerAsyncResult -> {
                System.out.println("http server start on 8080");
            });
        });*/
        WebClientOptions options = new WebClientOptions().setUserAgent("My-App/1.2.3");
        //options.setPipelining(true).setPipeliningLimit(1000);

        suite.test("test1", testContext -> {
            Async async = testContext.async();

            long start = System.currentTimeMillis();
            long s[] = new long[3];
            int n = 1000;
            AtomicLong al = new AtomicLong(0);
            IntStream.range(0, n).forEach(i -> {
                long starti = System.currentTimeMillis();
                WebClient client = WebClient.create(vertx, options);
                HttpRequest<Buffer> request = client.get(8080, "localhost", "/plaintext/aaa");
                request.send(
                        ar -> {
                            long endi = System.currentTimeMillis();
                            long cost = endi - starti;
                            if (cost > s[0]) {
                                s[0] = cost;
                            }
                            if (s[1] > cost || s[1] == 0) {
                                s[1] = cost;
                            }
                            s[2] += cost;
                            if (ar.succeeded()) {
                                System.out.println(Thread.currentThread() + " result code=" + ar.result().statusCode() + ",i=" + i + ", cost=" + cost);
                            } else {
                                System.out.println("failed");
                                if (ar.cause() != null) {
                                    ar.cause().printStackTrace();
                                }
                            }
                            AL.incrementAndGet();
                            if (al.incrementAndGet() >= n) {
                                System.out.println(Thread.currentThread() + " request complete");
                                System.out.println(Thread.currentThread() + " 最大响应时间=" + s[0] + ",最小响应时间=" + s[1] + ",平均响应时间=" + (s[2] / n));
                                async.complete();
                            }
                        });
            });
            async.awaitSuccess();
        });
        suite.run(new TestOptions().addReporter(new ReportOptions().setTo("console")));
    }
}
