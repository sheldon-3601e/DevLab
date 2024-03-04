package com.sheldon.devlab.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName RedissonConfig
 * @Author sheldon
 * @Date 2024/2/20 0:40
 * @Version 1.0
 * @Description TODO
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedissonConfig {

    private String host;

    private String port;

    private String password;

    @Bean
    public RedissonClient redissonClient() {
        // 1. Create config object
        Config config = new Config();
        String url = String.format("redis://%s:%s", host, port);
        config.useSingleServer()
                .setAddress(url)
                .setPassword(password)
                .setDatabase(4);
        // 2. Create Redisson instance

        // Sync and Async API
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
