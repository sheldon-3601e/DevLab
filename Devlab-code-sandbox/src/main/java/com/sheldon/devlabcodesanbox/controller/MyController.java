package com.sheldon.devlabcodesanbox.controller;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Random;

/**
 * @ClassName MyController
 * @Author 26483
 * @Date 2024/1/23 1:14
 * @Version 1.0
 * @Description 自己开发的模拟接口
 */
@RestController
@RequestMapping("/my")
public class MyController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    Random random = new Random();

    @GetMapping("/chicken_soup")
    public String getPoisonousChickenSoup(){
        String rValue = String.valueOf(random.nextInt(100));
        return stringRedisTemplate.opsForValue().get("myInterfaceInfo:ChickenSoup:" + rValue);

    }

    @GetMapping("/user_name")
    public String getUserName(){
        String rValue = String.valueOf(random.nextInt(100));
        return stringRedisTemplate.opsForValue().get("myInterfaceInfo:UserName:" + rValue);

    }

}
