package com.mindai.vertx.config;

import java.net.URL;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class ConfigTest extends AbstractVerticle{
	
	//初始化ConfigRetriever
	public ConfigRetriever getConfigRetriever() {
		String profile = System.getenv("vertx.profile.active");
		String config = "application-${profile}.yml";
		if(profile != null && profile.trim() != "") {
			config = config.replace("${profile}", profile);
		}else {
			config = config.replace("-${profile}", "");
		}
		return null;
	}

	public void start() throws Exception {
		URL url = ConfigTest.class.getResource("/config.yml");
		System.out.println("config file's path=" + url.getPath());
		ConfigStoreOptions store = new ConfigStoreOptions()
				.setType("file")
				.setFormat("yaml")
				.setConfig(new JsonObject().put("path", url.getPath()));
		
		ConfigRetriever retriever = ConfigRetriever.create(vertx,
			    new ConfigRetrieverOptions().addStore(store));
		retriever.getConfig(config -> {
			if(config.succeeded()) {
				System.out.println("get config: " + config.result());
			}else {
				System.err.println("get config err: " + config.cause());
			}
		});
		super.start();
	}
	
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		for(int i = 0; i < args.length; i++) {
			System.out.println("args[" + i + "]=" + args[i]);
		}
	}

}
