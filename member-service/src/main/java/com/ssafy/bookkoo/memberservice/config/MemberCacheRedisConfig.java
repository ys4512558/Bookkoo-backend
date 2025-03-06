package com.ssafy.bookkoo.memberservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class MemberCacheRedisConfig {

    @Value("${config.redis.cache.host}")
    private String REDIS_HOST;

    @Value("${config.redis.cache.port}")
    private int REDIS_PORT;
    @Bean
    public RedisConnectionFactory memberCacheRedisConnectionFactory() {
        return new LettuceConnectionFactory(REDIS_HOST, REDIS_PORT);
    }

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory memberCacheRedisConnectionFactory) {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                                                                       .entryTtl(Duration.ofDays(7))
                                                                       .disableCachingNullValues()
                                                                       .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                                                                       .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.builder(memberCacheRedisConnectionFactory)
                                .cacheDefaults(cacheConfiguration)
                                .build();
    }
}
