package com.sheldon.devlab.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sheldon.devlab.model.dto.user.UserMatchQueryRequest;
import com.sheldon.devlab.model.dto.user.UserQueryByTagRequest;
import com.sheldon.devlab.model.dto.user.UserQueryRequest;
import com.sheldon.devlab.model.entity.User;
import com.sheldon.devlab.model.vo.LoginUserVO;
import com.sheldon.devlab.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户服务
 *
 * @author <a href="https://github.com/sheldon-3601e">sheldon</a>
 * @from <a href="https://github.com/sheldon-3601e">sheldon</a>
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 获取当前登录用户（允许未登录）
     *
     * @param request
     * @return
     */
    User getLoginUserPermitNull(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    boolean isAdmin(User user);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取脱敏的已登录用户信息
     *
     * @return
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param userList
     * @return
     */
    List<UserVO> getUserVO(List<User> userList);

    /**
     * 获取查询条件
     *
     * @param userQueryRequest
     * @return
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 通过SQL查询用户
     *
     * @param tagNameList
     * @return
     */
    List<UserVO> searchUserByTagsUseSQL(List<String> tagNameList);

    /**
     * 通过内存查询用户
     *
     * @param tagNameList
     * @return
     */
    List<UserVO> searchUserByTagsUseMemory(List<String> tagNameList);

    /**
     * 筛选符合标签的用户
     *
     * @param tagNameList
     * @return
     */
    List<User> filtersUsersByTag(List<User> userList, List<String> tagNameList);

    /**
     * 将标签字符串转化为标签列表
     *
     * @param user
     * @return
     */
    List<String> getTags(User user);

    List<String> getTags(String tags);

    /**
     * 推荐用户预热
     * @return
     */
    List<UserVO> getRecommendUserList();

    /**
     * 查询匹配的用户列表
     * @param loginUser
     * @return
     */
    List<UserVO> listMatchUSerVO(UserMatchQueryRequest userMatchQueryRequest, User loginUser);

    /**
     * 根据标签和关键字查询用户
     * @param userQueryByTagRequest
     * @return
     */
    Page<UserVO> listUserVOByTagAndPage(UserQueryByTagRequest userQueryByTagRequest);
}
