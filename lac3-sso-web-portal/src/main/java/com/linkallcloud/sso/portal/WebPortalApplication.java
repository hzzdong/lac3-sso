package com.linkallcloud.sso.portal;

import java.util.List;

import javax.servlet.MultipartConfigElement;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.util.unit.DataSize;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.web.interceptors.LacEnvInterceptor;
import com.linkallcloud.web.support.AppVisitorArgumentResolver;
import com.linkallcloud.web.support.TraceArgumentResolver;

@EnableDubbo(multipleConfig = true)
@Configuration
@SpringBootApplication(scanBasePackages = { "com.linkallcloud.sso.redis", "com.linkallcloud.sso.ticket",
		"com.linkallcloud.sso.portal" })
public class WebPortalApplication implements WebMvcConfigurer, CommandLineRunner {
    private static final Log log = Logs.get();

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
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration envpi = registry.addInterceptor(getLacEnvInterceptor());
		envpi.excludePathPatterns("/js/**", "/css/**", "/images/**", "/img/**", "/static/**");
		envpi.addPathPatterns("/**");
		WebMvcConfigurer.super.addInterceptors(registry);
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

	@Bean
	public CookieSerializer cookieSerializer() {
		DefaultCookieSerializer serializer = new DefaultCookieSerializer();
		serializer.setCookieName("MYSESSION");
		serializer.setCookiePath("/");
		serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
		return serializer;
	}

	public static void main(String[] args) {
		SpringApplication.run(WebPortalApplication.class, args);
	}

	/**
	 * 文件上传配置
	 */
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		// 单个文件最大
		factory.setMaxFileSize(DataSize.parse("10240KB")); // KB,MB
		// 设置总上传数据总大小
		factory.setMaxRequestSize(DataSize.parse("102400KB"));
		return factory.createMultipartConfig();
	}

	@Override
	public void run(String... args) throws Exception {
		log.error("Track:WebPortalApplication服务器启动完成!");
	}

}
