package com.linkallcloud.sso.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;

@SpringBootApplication(scanBasePackages = { "com.linkallcloud.sso.redis", "com.linkallcloud.sso.ticket",
		"com.linkallcloud.sso.server" })
@EnableTransactionManagement
@EnableScheduling
@MapperScan(basePackages = "com.linkallcloud.sso.server.dao")
public class ServerApplication implements CommandLineRunner {
	private static final Log log = Logs.get();

	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters() {
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
		fastConverter.setFastJsonConfig(fastJsonConfig);
		return new HttpMessageConverters(fastConverter);
	}

//	@Bean
//	public RestTemplate restTemplate() {
//		RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
//		return restTemplateBuilder.build();
//	}

	public static void main(String[] args) {
		new SpringApplicationBuilder(ServerApplication.class).web(WebApplicationType.NONE).run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.error("Track:ServerApplication服务器启动完成!");
	}

}
