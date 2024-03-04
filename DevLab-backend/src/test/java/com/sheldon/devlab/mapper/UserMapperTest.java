package com.sheldon.devlab.mapper;

import com.sheldon.devlab.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @ClassName UserMapperTest
 * @Author sheldon
 * @Date 2024/2/26 17:13
 * @Version 1.0
 * @Description TODO
 */
@SpringBootTest
public class UserMapperTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void test() {
        // 测试批量插入用户列表
        for (int i = 0; i < 100000; i++) {
            User user = new User();
            user.setUserAccount("account" + i);
            user.setUserPassword("password" + i);
            user.setUserName("name" + i);
            user.setUserGender("1");
            user.setTags("[]");
            userMapper.insert(user);
        }
    }

}
