package com.foodapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);
    @Value("${spring.data.redis.host}")
    private String redisHost;
    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        logger.info("redisHost:{}, redisPort:{}", redisHost, redisPort);
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        // Set the connection factory, this ensures that RedisTemplate can connect to the redis server
        template.setConnectionFactory(lettuceConnectionFactory());

        // Redis stores data as binary, so serialization is required
        // StringRedisSerializer ensures that keys are stored as readable UTF-8 strings instead of raw bytes
        template.setKeySerializer(new StringRedisSerializer());
        // Tell redis to store values in JSON format
        // It convert Java object to JSON when persist and back to Java object when retrieve
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));

        // Config when storing hash structure in Redis
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));

        //Ensure the configuration is all set
        template.afterPropertiesSet();
        return template;
    }
}
