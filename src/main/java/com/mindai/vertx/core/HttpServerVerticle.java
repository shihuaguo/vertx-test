package com.mindai.vertx.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;

public class HttpServerVerticle extends AbstractVerticle {

    public static void main(String[] args) {
        DeploymentOptions options = new DeploymentOptions();
        options.setInstances(4);
        Vertx.vertx().deployVerticle(HttpServerVerticle.class.getName(), options);
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        HttpServer server = vertx.createHttpServer();
        server.requestHandler(req -> {
            req.response().end("hello,world");
        });
        server.listen(8090, "localhost", res -> {
            if (res.succeeded()) {
                System.out.println("httpserver listen on 8090 success");
            } else {
                System.err.println("httpserver listen on 8090 failed");
            }
        });
        super.start(startFuture);
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        super.stop(stopFuture);
    }

}
