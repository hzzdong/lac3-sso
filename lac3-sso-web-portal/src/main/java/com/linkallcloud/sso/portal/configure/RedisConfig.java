package com.linkallcloud.sso.portal.configure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.linkallcloud.sso.portal.ticket.LoginTicket;
import com.linkallcloud.sso.portal.ticket.ProxyGrantingTicket;
import com.linkallcloud.sso.portal.ticket.ProxyTicket;
import com.linkallcloud.sso.portal.ticket.ServiceTicket;
import com.linkallcloud.sso.portal.ticket.TicketGrantingTicket;

@Configuration
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig {
	@Bean
	@ConditionalOnMissingBean(name = "redisTGTTemplate")
	public RedisTemplate<String, TicketGrantingTicket> redisTGTTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, TicketGrantingTicket> template = new RedisTemplate<>();
		// 使用fastjson序列化
		FastJsonRedisSerializer<TicketGrantingTicket> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(
				TicketGrantingTicket.class);
		// value值的序列化采用fastJsonRedisSerializer
		template.setValueSerializer(fastJsonRedisSerializer);
		template.setHashValueSerializer(fastJsonRedisSerializer);
		// key的序列化采用StringRedisSerializer
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}

	@Bean
	@ConditionalOnMissingBean(name = "redisSTTemplate")
	public RedisTemplate<String, ServiceTicket> redisSTTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, ServiceTicket> template = new RedisTemplate<>();
		// 使用fastjson序列化
		FastJsonRedisSerializer<ServiceTicket> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(
				ServiceTicket.class);
		// value值的序列化采用fastJsonRedisSerializer
		template.setValueSerializer(fastJsonRedisSerializer);
		template.setHashValueSerializer(fastJsonRedisSerializer);
		// key的序列化采用StringRedisSerializer
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}

	@Bean
	@ConditionalOnMissingBean(name = "redisPTTemplate")
	public RedisTemplate<String, ProxyTicket> redisPTTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, ProxyTicket> template = new RedisTemplate<>();
		// 使用fastjson序列化
		FastJsonRedisSerializer<ProxyTicket> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(ProxyTicket.class);
		// value值的序列化采用fastJsonRedisSerializer
		template.setValueSerializer(fastJsonRedisSerializer);
		template.setHashValueSerializer(fastJsonRedisSerializer);
		// key的序列化采用StringRedisSerializer
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}

	@Bean
	@ConditionalOnMissingBean(name = "redisPGTTemplate")
	public RedisTemplate<String, ProxyGrantingTicket> redisPGTTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, ProxyGrantingTicket> template = new RedisTemplate<>();
		// 使用fastjson序列化
		FastJsonRedisSerializer<ProxyGrantingTicket> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(
				ProxyGrantingTicket.class);
		// value值的序列化采用fastJsonRedisSerializer
		template.setValueSerializer(fastJsonRedisSerializer);
		template.setHashValueSerializer(fastJsonRedisSerializer);
		// key的序列化采用StringRedisSerializer
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}

	@Bean
	@ConditionalOnMissingBean(name = "redisLtTemplate")
	public RedisTemplate<String, LoginTicket> redisLtTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, LoginTicket> template = new RedisTemplate<>();
		// 使用fastjson序列化
		FastJsonRedisSerializer<LoginTicket> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(LoginTicket.class);
		// value值的序列化采用fastJsonRedisSerializer
		template.setValueSerializer(fastJsonRedisSerializer);
		template.setHashValueSerializer(fastJsonRedisSerializer);
		// key的序列化采用StringRedisSerializer
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}

	@Bean
	@ConditionalOnMissingBean(name = "redisTemplate")
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<Object, Object> template = new RedisTemplate<>();
		// 使用fastjson序列化
		FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<Object>(Object.class);
		// value值的序列化采用fastJsonRedisSerializer
		template.setValueSerializer(fastJsonRedisSerializer);
		template.setHashValueSerializer(fastJsonRedisSerializer);
		// key的序列化采用StringRedisSerializer
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}

	@Bean
	@ConditionalOnMissingBean(StringRedisTemplate.class)
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}

}
