package com.sheldon.devlab.job.cycle;

import com.sheldon.devlab.service.UserService;
import com.sheldon.devlab.model.dto.user.UserMatchQueryRequest;
import com.sheldon.devlab.model.entity.User;
import com.sheldon.devlab.model.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName ProRecommendUserList
 * @Author sheldon
 * @Date 2024/2/19 15:54
 * @Version 1.0
 * @Description 预热推荐用户列表
 */
@Component
@Slf4j
public class ProRecommendUserList {

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private UserService userService;

    @Scheduled(cron = "0 0 0 1/1 * ? ")
    public void proRecommendUserList() {
        // 获取锁
        RLock lock = redissonClient.getLock("devlab:user:recommend:list:lock");

        long id = Thread.currentThread().getId();
        try {
            // 获取锁 参数：获取锁的最大等待时间(期间会重试)，锁自动释放时间，时间单位
            boolean isLock = lock.tryLock(0, -1, TimeUnit.MILLISECONDS);
            if (isLock) {
                log.info(id + "get lock success");
                // 1. 获取推荐用户
                UserMatchQueryRequest userMatchQueryRequest = new UserMatchQueryRequest();
                userMatchQueryRequest.setMatchNum(32);
                User user = userService.getById(1L);
                List<UserVO> matchUserVOList = userService.listMatchUSerVO(userMatchQueryRequest, user);
                String key = "devlab:user:devlab:list:" + user.getId() ;
                redisTemplate.opsForValue().set(key, matchUserVOList, 24, TimeUnit.HOURS);
                user = userService.getById(2L);
                matchUserVOList = userService.listMatchUSerVO(userMatchQueryRequest, user);
                key = "devlab:user:devlab:list:" + user.getId() ;
                redisTemplate.opsForValue().set(key, matchUserVOList, 24, TimeUnit.HOURS);
            } else {
                log.info(id + "get lock fail");
            }
        } catch (Exception e) {
            log.error("get lock error", e);
        } finally {
            // 释放锁
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                log.info(id + "release lock");
                lock.unlock();
            }
        }


    }

}
