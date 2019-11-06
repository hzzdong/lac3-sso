package com.linkallcloud.sso.oapi;

import java.util.List;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.linkallcloud.web.interceptors.LacEnvInterceptor;
import com.linkallcloud.web.support.AppVisitorArgumentResolver;
import com.linkallcloud.web.support.TraceArgumentResolver;

@EnableDubbo(multipleConfig = true)
@Configuration
@SpringBootApplication(scanBasePackages = { "com.linkallcloud.sso.oapi" })
public class WebOapiApplication implements WebMvcConfigurer {

	@Value("${lac.static.server}")
	private String staticServer;
	@Value("${lac.static.res.version}")
	private String staticResourceVersion;

	@Bean
	public LacEnvInterceptor getLacEnvInterceptor() {
		return new LacEnvInterceptor() {
			@Override
			protected String getStaticServer() {
				return staticServer;
			}

			@Override
			protected String getResourceVersion() {
				return staticResourceVersion;
			}
		};
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new AppVisitorArgumentResolver());
		resolvers.add(new TraceArgumentResolver());
		WebMvcConfigurer.super.addArgumentResolvers(resolvers);
	}

	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters() {
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
		fastConverter.setFastJsonConfig(fastJsonConfig);
		return new HttpMessageConverters(fastConverter);
	}

	public static void main(String[] args) {
		SpringApplication.run(WebOapiApplication.class, args);
	}

}
