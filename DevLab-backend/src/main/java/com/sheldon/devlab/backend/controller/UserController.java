package com.sheldon.devlab.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sheldon.devlab.backend.annotation.AuthCheck;
import com.sheldon.devlab.backend.common.BaseResponse;
import com.sheldon.devlab.backend.common.DeleteRequest;
import com.sheldon.devlab.backend.common.ErrorCode;
import com.sheldon.devlab.backend.common.ResultUtils;
import com.sheldon.devlab.backend.constant.UserConstant;
import com.sheldon.devlab.backend.exception.BusinessException;
import com.sheldon.devlab.backend.exception.ThrowUtils;
import com.sheldon.devlab.backend.model.dto.user.*;
import com.sheldon.devlab.backend.model.entity.User;
import com.sheldon.devlab.backend.model.vo.LoginUserVO;
import com.sheldon.devlab.backend.model.vo.UserVO;
import com.sheldon.devlab.backend.service.UserService;
import com.sheldon.devlab.backend.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户接口
 *
 * @author <a href="https://github.com/sheldon-3601e">sheldon</a>
 * @from <a href="https://github.com/sheldon-3601e">sheldon</a>
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate redisTemplate;

    // region 登录相关

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(user));
    }

    // endregion

    // region 增删改查

    /**
     * 创建用户
     *
     * @param userAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest, HttpServletRequest request) {
        if (userAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        // 默认密码 12345678
        String defaultPassword = "12345678";
        String encryptPassword = DigestUtils.md5DigestAsHex((UserServiceImpl.SALT + defaultPassword).getBytes());
        user.setUserPassword(encryptPassword);
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(user.getId());
    }

    /**
     * 删除用户
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 更新用户
     *
     * @param userUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest,
                                            HttpServletRequest request) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取用户（仅管理员）
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    /**
     * 根据 id 获取包装类
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(long id, HttpServletRequest request) {
        BaseResponse<User> response = getUserById(id, request);
        User user = response.getData();
        return ResultUtils.success(userService.getUserVO(user));
    }

    /**
     * 分页获取用户列表（仅管理员）
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<User>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest,
                                                   HttpServletRequest request) {
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        return ResultUtils.success(userPage);
    }

    /**
     * 分页获取用户封装列表
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest,
                                                       HttpServletRequest request) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        Page<UserVO> userVOPage = new Page<>(current, size, userPage.getTotal());
        List<UserVO> userVO = userService.getUserVO(userPage.getRecords());
        userVOPage.setRecords(userVO);
        return ResultUtils.success(userVOPage);
    }

    // endregion

    /**
     * 更新个人信息
     *
     * @param userUpdateMyRequest
     * @param request
     * @return
     */
    @PostMapping("/update/my")
    public BaseResponse<Boolean> updateMyUser(@RequestBody UserUpdateMyRequest userUpdateMyRequest,
                                              HttpServletRequest request) {
        if (userUpdateMyRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        List<String> tags = userUpdateMyRequest.getTags();
        if (CollectionUtils.isEmpty(tags)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String tagsString = tags.toString();
        User user = new User();
        BeanUtils.copyProperties(userUpdateMyRequest, user);
        user.setId(loginUser.getId());
        user.setTags(tagsString);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 通过标签和个人信息查询用户包装类列表
     *
     * @param userQueryByTagRequest
     * @param request
     * @return
     */
    @PostMapping("/list/tag/page/vo")
    public BaseResponse<Page<UserVO>> listUserVOByTagAndPage(@RequestBody UserQueryByTagRequest userQueryByTagRequest,
                                                             HttpServletRequest request) {
        if (userQueryByTagRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long size = userQueryByTagRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<UserVO> userVOPage = userService.listUserVOByTagAndPage(userQueryByTagRequest);

        return ResultUtils.success(userVOPage);
    }

    /**
     * 查询匹配的用户
     *
     * @param request
     * @return
     */
    @PostMapping("/list/match/page/vo")
    public BaseResponse<Page<UserVO>> listMatchUSerVO(@RequestBody UserMatchQueryRequest userMatchQueryRequest, HttpServletRequest request) {

        if (userMatchQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 验证是否登录
        User loginUser = userService.getLoginUser(request);
        long current = userMatchQueryRequest.getCurrent();
        long size = userMatchQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);

        // 如果存在缓存，则直接读取缓存
        ValueOperations<String, List<UserVO>> opsForValue = redisTemplate.opsForValue();
        String key = "devlab:user:match:list:userList";
        List<UserVO> matchUserVOList = opsForValue.get(key);
        if (matchUserVOList == null) {
            // 如果没有缓存，则查询数据库，并存入缓存
            matchUserVOList = userService.listMatchUSerVO(userMatchQueryRequest, loginUser);
            opsForValue.set(key, matchUserVOList);
        }
        // 对 matchUserVOList 进行分页处理
        int fromIndex = (int) ((current - 1) * size);
        int toIndex = Math.min((int) (current * size), matchUserVOList.size());
        List<UserVO> pageList = matchUserVOList.subList(fromIndex, toIndex);

        // 创建分页对象
        Page<UserVO> userVOPage = new Page<>(current, size);
        userVOPage.setRecords(pageList);
        return ResultUtils.success(userVOPage);
    }
}
