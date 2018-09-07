package com.mindai.vertx.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

public class ChainNextHanlder extends AbstractVerticle {

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		HttpServer server = vertx.createHttpServer();
		Router router = Router.router(vertx);
		Route route1 = router.route("/mindai/query").handler(routingContext ->{
			routingContext.response().setChunked(true).write("route1\\n");
			
			routingContext.vertx().setTimer(2000, handler -> {
				routingContext.next();
			});
		});
		Route route2 = router.route("/mindai/query").handler(routingContext ->{
			routingContext.response().write("route2\\n");
			
			routingContext.vertx().setTimer(2000, handler -> {
				routingContext.next();
			});
		});
		Route route3 = router.route("/mindai/query").handler(routingContext ->{
			routingContext.response().write("route3");
			routingContext.response().end();
		});
		
		server.requestHandler(router::accept).listen(8080);
		super.start(startFuture);
	}

	@Override
	public void stop(Future<Void> stopFuture) throws Exception {
		super.stop(stopFuture);
	}
	
	public static void main(String[] args) {
		Vertx.vertx().deployVerticle(new ChainNextHanlder());
	}

}
