package com.mindai.vertx.test;

import java.util.Random;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;

public class VertxComposition {
	public static void main( String[] args ) {
		Vertx vertx = Vertx.vertx();
		//vertx.deployVerticle(new Verticle1());
		vertx.deployVerticle(new SequentialVerticle());
	}
	
	static class Verticle1 extends AbstractVerticle{

		@Override
		public void start(Future<Void> startFuture) throws Exception {
			super.start(startFuture);
			Future<String> sf = Future.future();
			Handler<Future<String>> h1 = f -> {
				int v = new Random().nextInt();
				System.out.println(Thread.currentThread());
				if(v % 2 == 0) {
					f.complete("hello");
				}else {
					f.fail("fail because random");
				}
			};
			h1.handle(sf);
			Future<Integer> ifu = Future.future();
			Handler<Future<Integer>> h2 = i -> {
				System.out.println(Thread.currentThread());
				i.complete(1);
			};
			h2.handle(ifu);
			CompositeFuture.all(sf, ifu).setHandler(ar->{
				if(ar.succeeded()) {
					System.out.println("success");
				}else {
					System.out.println("failed");
				}
			});
		}
	}
	
	static class SequentialVerticle extends AbstractVerticle{
		@Override
		public void start(Future<Void> startFuture) throws Exception {
			FileSystem fs = vertx.fileSystem();
			String path = "d:/tmp/a1.txt";
			Future<Void> f0 = Future.future();
			Future<Void> f1 = Future.future();
			fs.createFile(path, f1.completer());
			f1.compose(v->{
				Future<Void> f2 = Future.future();
				fs.writeFile(path, Buffer.buffer("hello,world!"), f2.completer());
				return f2;
			}).compose(v -> {
				fs.move(path, "d:/tmp/a2.txt", f0.completer());
			}, f0);
			System.out.println("f0 is success: " + f0.succeeded());
		}
	}
}
