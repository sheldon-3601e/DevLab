package com.sheldon.devlab.backend.service;

import cn.hutool.core.lang.Assert;
import com.sheldon.devlab.backend.model.entity.User;
import com.sheldon.devlab.backend.model.vo.UserVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;


/**
 * 用户服务测试
 *
 * @author <a href="https://github.com/sheldon-3601e">sheldon</a>
 * @from <a href="https://github.com/sheldon-3601e">sheldon</a>
 */
@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void userRegister() {
        String userAccount = "yupi";
        String userPassword = "";
        String checkPassword = "123456";
        try {
            long result = userService.userRegister(userAccount, userPassword, checkPassword);
            Assertions.assertEquals(-1, result);
            userAccount = "yu";
            result = userService.userRegister(userAccount, userPassword, checkPassword);
            Assertions.assertEquals(-1, result);
        } catch (Exception e) {

        }
    }

    @Test
    void userLogin() {
    }

    @Test
    void userLoginByMpOpen() {
    }

    @Test
    void getLoginUser() {
    }

    @Test
    void getLoginUserPermitNull() {
    }

    @Test
    void isAdmin() {
    }

    @Test
    void testIsAdmin() {
    }

    @Test
    void userLogout() {
    }

    @Test
    void getLoginUserVO() {
    }

    @Test
    void getUserVO() {
    }

    @Test
    void testGetUserVO() {
    }

    @Test
    void getQueryWrapper() {
    }

    @Test
    void searchUserByTagsUseSQL() {
        List<String> tagNameList = Arrays.asList("java", "python");
        List<UserVO> userVOList = userService.searchUserByTagsUseSQL(tagNameList);
        Assert.notEmpty(userVOList);
    }

    @Test
    void searchUserByTagsUseMemory() {
        List<String> tagNameList = Arrays.asList("java", "python");
        List<UserVO> userVOList = userService.searchUserByTagsUseMemory(tagNameList);
        Assert.notEmpty(userVOList);
    }

    /**
     * 测试批量导入用户 + 直接单条插入用户
     */
//    @Test
    public void testBatchImportUser01() {
        final int Num = 10000;
        // 记录开始时间
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < Num; i++) {
            User user = new User();
            user.setUserAccount("fakeSheldon" + i);
            user.setUserPassword("12345678");
            user.setUserName("fakeSheldon" + i);
            user.setUserGender("1");
            user.setUserAvatar("https://upen.caup.net/ai_pics_mj/202303/1677952366325269.jpg");
            user.setUserProfile("test profile");
            user.setTags("['Java', 'Python', 'C++']");
            user.setUserPhone("123");
            user.setUserEmail("123@qq.com");
            user.setUserRole("user");
            userService.save(user);
        }
        stopWatch.stop();
        System.out.println("批量导入" + Num + "条数据，耗时：" + stopWatch.getTotalTimeMillis() + "ms");
        // 批量导入10000条数据，耗时：44812ms
    }

    /**
     * 测试批量导入用户 + 批量插入用户
     */
//    @Test
    public void testBatchImportUser02() {
        final int USERS_NUM = 10000;
        final int BATCH_SIZE = 1000;
        // 记录开始时间
        StopWatch stopWatch = new StopWatch();
        List<User> list = new ArrayList<>();
        stopWatch.start();
        for (int i = 0; i < USERS_NUM; i++) {
            User user = new User();
            user.setUserAccount("fakeSheldon" + i);
            user.setUserPassword("12345678");
            user.setUserName("fakeSheldon" + i);
            user.setUserGender("1");
            user.setUserAvatar("https://upen.caup.net/ai_pics_mj/202303/1677952366325269.jpg");
            user.setUserProfile("test profile");
            user.setTags("['Java', 'Python', 'C++']");
            user.setUserPhone("123");
            user.setUserEmail("123@qq.com");
            user.setUserRole("user");
            list.add(user);
        }
        userService.saveBatch(list, BATCH_SIZE);
        stopWatch.stop();
        System.out.println("批量导入" + USERS_NUM + "条数据，耗时：" + stopWatch.getTotalTimeMillis() + "ms");
        // 批量导入10000条数据，耗时：4195ms
    }

    /**
     * 测试批量导入用户 + 多线程
     */
//    @Test
    public void testBatchImportUser03() {
        final int USERS_NUM = 10000;
        final int BATCH_SIZE = 500;
        // 记录开始时间
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        ExecutorService executorService = Executors.newFixedThreadPool(40);

        try {
            for (int i = 0; i < 20; i++) {
                // 每个线程处理一个子列表
                List<User> sublist = new ArrayList<>();
                for (int j = i * (USERS_NUM / 40); j < (i + 1) * (USERS_NUM / 40); j++) {
                    User user = new User();
                    user.setUserAccount("fakeSheldon" + j);
                    user.setUserPassword("12345678");
                    user.setUserName("fakeSheldon" + j);
                    user.setUserGender("1");
                    user.setUserAvatar("https://upen.caup.net/ai_pics_mj/202303/1677952366325269.jpg");
                    user.setUserProfile("test profile");
                    user.setTags("['Java', 'Python', 'C++']");
                    user.setUserPhone("123");
                    user.setUserEmail("123@qq.com");
                    user.setUserRole("user");
                    sublist.add(user);
                }
                // 将当前任务添加到任务队列
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> userService.saveBatch(sublist, BATCH_SIZE), executorService);
            }
            // 等待所有任务完成
            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("任务被中断");
        }

        stopWatch.stop();
        System.out.println("批量导入" + USERS_NUM + "条数据，耗时：" + stopWatch.getTotalTimeMillis() + "ms");
        // 批量导入10000条数据，耗时：1223ms
    }


}
