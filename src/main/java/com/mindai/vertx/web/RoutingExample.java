package com.mindai.vertx.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

public class RoutingExample extends AbstractVerticle {

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		HttpServer server = vertx.createHttpServer();
		Router router = Router.router(vertx);
		
		//routing by exact path
		router.route("/by_path/exactpath").handler(routingContext -> {
			routingContext.response().end("return by exact path");
		});
		
		//routing by paths that begin with something
		router.route("/by_path/exactpath/*").handler(routingContext ->{
			routingContext.response().end("return by paths that begin with something");
		});
		
		//Capturing path parameters
		router.route("/capture_parameter/:param1/:param2").handler(routingContext ->{
			String param1 = routingContext.request().getParam("param1");
			String param2 = routingContext.request().getParam("param2");
			System.out.println("request with param1=" + param1 + ",param2=" + param2);
			routingContext.response().end("return by paths that contains parameters");
		});
		
		//Capturing path parameters with regular expressions
		Route route = router.patchWithRegex(".*foo");
		// This regular expression matches paths that start with something like:
		// "/foo/bar" - where the "foo" is captured into param0 and the "bar" is captured into
		// param1
		route.pathRegex("\\/([^\\/]+)\\/([^\\/]+)").handler(routingContext -> {

		  String productType = routingContext.request().getParam("param0");
		  String productID = routingContext.request().getParam("param1");
		  routingContext.response().end("Capturing path parameters with regular expressions,"
		  		+ "param0=" + productType + ",param1=" + productID);

		});
		
		//routing by http method
		router.route(HttpMethod.GET, "/by_method/get").handler(routingContext -> {
			routingContext.response().end("return by get method");
		});
		
		router.route("/by_method/getorpost").method(HttpMethod.GET).method(HttpMethod.POST).handler(routingContext ->{
			routingContext.response().end("return by get/post method");
		});
		
		//by MIME type
		router.route("/by_mine_type/*").consumes("text/html").consumes("text/plain").handler(routingContext ->{
			routingContext.response().end("return by mine type(text/html,text/plain)");
		});
		
		//Routing based on MIME types acceptable by the client
		router.route("/by_mine_type_acceptable").produces("text/html").produces("application/json").handler(routingContext ->{
			String contentType = routingContext.getAcceptableContentType();
			HttpServerResponse response = routingContext.response();
			response.putHeader("content-type", contentType);
			response.write("return mine type acceptable by client").end();
		});
		server.requestHandler(router::accept).listen(8080);
		super.start(startFuture);
	}

	@Override
	public void stop(Future<Void> stopFuture) throws Exception {
		super.stop(stopFuture);
	}
	
	public static void main(String[] args) {
		Vertx.vertx().deployVerticle(new RoutingExample());
	}

}
