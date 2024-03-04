package com.sheldon.devlab.utils;

import com.sheldon.devlab.model.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

/**
 * @ClassName RedisTest
 * @Author sheldon
 * @Date 2024/2/19 15:32
 * @Version 1.0
 * @Description Redis 配置测试
 */
@SpringBootTest
class RedisTest {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void test() {
        ValueOperations opsForValue = redisTemplate.opsForValue();
        opsForValue.set("name", "sheldon");
        User user = new User();
        user.setId(1L);
        user.setUserAccount("111");
        user.setUserPassword("111");
        user.setUserName("2222");
        opsForValue.set("user", user);

        Object name = opsForValue.get("name");
        Assertions.assertEquals("sheldon", (String) name);
        Object user1 = opsForValue.get("user");
        System.out.println(user1);

    }
}
