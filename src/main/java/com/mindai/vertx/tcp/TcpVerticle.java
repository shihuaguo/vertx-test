package com.mindai.vertx.tcp;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.NetSocket;

public class TcpVerticle extends AbstractVerticle {

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		//super.start(startFuture);
		NetServerOptions option = new NetServerOptions().setPort(9001);
		NetServer server = vertx.createNetServer(option);
		server.connectHandler(socket -> {
			socket.handler(buffer ->{
				System.out.println("I received some bytes: " + buffer.length() + " from " + socket.remoteAddress());
				System.out.println("content is: " + buffer.toString());
			});
		});
		server.listen(res->{
			if(res.succeeded()) {
				System.out.println("server is listening");
			}else {
				System.out.println("server listen error");
				startFuture.fail(res.cause());
			}
		});
		
		//create client
		NetClientOptions clientOptions = new NetClientOptions();
		clientOptions.setConnectTimeout(100000);
		NetClient client = vertx.createNetClient(clientOptions);
		client.connect(9001, "localhost", res -> {
			if(res.succeeded()) {
				System.out.println("connect to localhost:9000 success");
				NetSocket socket = res.result();
				final int[] a = new int[] {1};
				//发送10次请求,然后取消
				vertx.setPeriodic(1000, id->{
					socket.write("Hello,this is " + a[0]);
					a[0]++;
					if(a[0] >= 10) {
						vertx.cancelTimer(id);
					}
				});
			}else {
				System.out.println("connect to localhost:9000 error");
			}
		});
	}

	@Override
	public void stop(Future<Void> stopFuture) throws Exception {
		super.stop(stopFuture);
	}
	
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new TcpVerticle());
	}

}
