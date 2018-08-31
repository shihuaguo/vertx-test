package com.mindai.vertx.test;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;

public class AsyncVerticle extends AbstractVerticle {

	private HttpServer server;

	public void start(Future<Void> startFuture) {
		server = vertx.createHttpServer().requestHandler(req -> {
			req.response().putHeader("content-type", "text/plain").end("Hello from Vert.x!");
		});

		// Now bind the server:
		server.listen(8080, res -> {
			if (res.succeeded()) {
				startFuture.complete();
			} else {
				startFuture.fail(res.cause());
			}
		});
	}

	public void stop(Future<Void> stopFuture) {
		/*
		 * obj.doSomethingThatTakesTime(res -> { if (res.succeeded()) {
		 * stopFuture.complete(); } else { stopFuture.fail(); } });
		 */
	}
}