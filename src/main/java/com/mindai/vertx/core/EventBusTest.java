package com.mindai.vertx.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;

public class EventBusTest extends AbstractVerticle{

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		super.start(startFuture);
		System.out.println("EventBusTest,vertx=" + vertx);
		//registering handlers
		EventBus eb = vertx.eventBus();
		eb.consumer("mindai.vertx.test", message -> {
			  System.out.println("I have received a message: " + message.body());
		});
		
		MessageConsumer<String> consumer = eb.consumer("mindai.vertx.test");
		consumer.handler(message -> {
		  System.out.println("I have received a message: " + message.body());
		});
	}
	
	static class Verticle1 extends AbstractVerticle{

		@Override
		public void start() throws Exception {
			super.start();
			System.out.println("Verticle1 vertx=" + vertx);
			EventBus eb = vertx.eventBus();
			eb.publish("mindai.vertx.test","hello,world!");	
		}
	}
	
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		System.out.println("main,vertx=" + vertx);
		vertx.deployVerticle(new EventBusTest());
		vertx.deployVerticle(new Verticle1());
	}

}
