//package com.hiringwire;
//
//import com.hiringwire.dto.JobDTO;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//
//import java.time.Duration;
//
//@Configuration  // Marks this class as a Spring configuration class
//public class RedisConfig {
//
//    @Bean  // Defines a Spring bean for RedisCacheManager
//    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
//        // Define cache configuration
//        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(10)) // Set time-to-live (TTL) for cache entries to 10 minutes
//                .disableCachingNullValues() // Prevent caching of null values
//                .serializeValuesWith(RedisSerializationContext.SerializationPair
//                        .fromSerializer(new Jackson2JsonRedisSerializer<>(JobDTO.class))); // Serialize values using Jackson JSON serializer
//
//        // Create and return a RedisCacheManager with the specified configuration
//        return RedisCacheManager.builder(redisConnectionFactory)
//                .cacheDefaults(cacheConfig) // Apply default cache configuration
//                .build();
//    }
//}