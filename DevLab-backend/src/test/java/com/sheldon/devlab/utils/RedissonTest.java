package com.sheldon.devlab.utils;

import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @ClassName RedissonTest
 * @Author 26483
 * @Date 2024/2/20 0:45
 * @Version 1.0
 * @Description TODO
 */
@SpringBootTest
class RedissonTest {

    @Resource
    private RedissonClient redissonClient;

    @Test
    void test() {
        RList<String> list = redissonClient.getList("test-list");
        list.add("sheldon");
        String s = list.get(0);
        System.out.println("s = " + s);
        list.remove(0);
    }

}
