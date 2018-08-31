package com.mindai.vertx.test;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(2));
    	long timer = vertx.setPeriodic(1000, id -> {
    		System.out.println("Hello, world!");
    	});
    	boolean b = vertx.cancelTimer(timer);
    	System.out.println("b=" + b);
    }
}
