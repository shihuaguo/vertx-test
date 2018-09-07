package com.mindai.vertx.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;

public class HttpServerVerticle extends AbstractVerticle {

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		HttpServer server = vertx.createHttpServer();
		server.requestHandler(req -> {
			System.out.println("http request receive:");
			System.out.println("version: " + req.version());
			System.out.println("method: " + req.method());
			System.out.println("uri: " + req.uri());
			if(req.method() == HttpMethod.POST) {
				req.bodyHandler(buffer -> {
					System.out.println("receive body: " + buffer.toString());
				});
			}
			req.response().end("hello,world");
		});
		server.listen(8001, "localhost", res -> {
			if(res.succeeded()) {
				System.out.println("httpserver listen on 8001 success");
			}else {
				System.err.println("httpserver listen on 8001 failed");
			}
		});
		super.start(startFuture);
	}

	@Override
	public void stop(Future<Void> stopFuture) throws Exception {
		super.stop(stopFuture);
	}
	
	public static void main(String[] args) {
		Vertx.vertx().deployVerticle(new HttpServerVerticle());
	}

}
