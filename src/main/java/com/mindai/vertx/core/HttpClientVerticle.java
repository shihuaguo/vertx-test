package com.mindai.vertx.core;

import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

public class HttpClientVerticle extends AbstractVerticle {
	
	public static final int VERTICLE_INSTANCE = 8;
	
	public static AtomicLong AL = new AtomicLong(0);

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		WebClientOptions options = new WebClientOptions().setUserAgent("My-App/1.2.3");
		options.setKeepAlive(true).setMaxPoolSize(100).setPipelining(true).setPipeliningLimit(1000);
		WebClient client = WebClient.create(vertx, options);
		HttpRequest<Buffer> request = client.post(443, "testbank.mindai.com", "/member/goodsList")
				//			.addQueryParam("method", "general.getPcProjectList")
				//			.addQueryParam("appKey", "00000001")
				//			.addQueryParam("v", "2.5.0")
				//			.addQueryParam("format", "json")
				//			.addQueryParam("locale", "cn")
				//			.addQueryParam("sessionId", "")
				//			.addQueryParam("timestamp", "2018-09-20 17:00:05")
				//			.addQueryParam("bizContent", "FmZ6suAyUgKO6XBaZ5pX0w==")
				//			.addQueryParam("sign", "9953CC22EF162458E994ACDBDCAE04F5576BCF11")
				.ssl(true);
		
		long start = System.currentTimeMillis();
		long s[] = new long[3];
		int n = 100;
		AtomicLong al = new AtomicLong(0);
		IntStream.range(0, n).forEach(i -> {
			long starti = System.currentTimeMillis();
			request
			.sendJsonObject(new JsonObject()
					.put("goodsType", "0")
					.put("pageNum", "0")
					.put("pageSize", "6"), 
					ar ->{
				long endi = System.currentTimeMillis();
				long cost = endi - starti;
				if(cost > s[0]) {
					s[0] = cost;
				}
				if(s[1] > cost || s[1] == 0) {
					s[1] = cost;
				}
				s[2] += cost;
				if(ar.succeeded()) {
					System.out.println(Thread.currentThread() + " result code=" + ar.result().statusCode() + ",i=" + i +  ", cost=" + cost);
					//System.out.println("body: " + ar.result().bodyAsString());
				}else {
					System.out.println("failed");
					if(ar.cause() != null) {
						ar.cause().printStackTrace();
					}
				}
				AL.incrementAndGet();
				if(al.incrementAndGet() >= n) {
					System.out.println(Thread.currentThread() + " request complete");
					System.out.println(Thread.currentThread() + " 最大响应时间=" + s[0] +",最小响应时间=" + s[1] + ",平均响应时间=" + (s[2]/n));
					if(AL.get() >= VERTICLE_INSTANCE * n) {
						vertx.close();
					}
				}
			});
		});
		System.out.println("发送" + n + " 个请求消耗: " + (System.currentTimeMillis() - start));
		super.start(startFuture);
	}

	@Override
	public void stop(Future<Void> stopFuture) throws Exception {
		super.stop(stopFuture);
	}
	
	public static void main(String[] args) {
		DeploymentOptions dp = new DeploymentOptions().setInstances(VERTICLE_INSTANCE);
		Vertx.vertx().deployVerticle(HttpClientVerticle.class.getName(), dp);
	}

}
