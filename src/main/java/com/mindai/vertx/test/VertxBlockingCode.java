package com.mindai.vertx.test;

import io.vertx.core.Vertx;

public class VertxBlockingCode {

	public static void main( String[] args ) {
		Vertx vertx = Vertx.vertx();
		vertx.executeBlocking(b->{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			b.complete("hello");
		}, r->{
			System.out.println(r);
		});
	}
	
}
