package com.sheldon.devlab.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sheldon.devlab.common.ErrorCode;
import com.sheldon.devlab.constant.CommonConstant;
import com.sheldon.devlab.exception.BusinessException;
import com.sheldon.devlab.mapper.UserMapper;
import com.sheldon.devlab.model.dto.user.UserMatchQueryRequest;
import com.sheldon.devlab.model.dto.user.UserQueryByTagRequest;
import com.sheldon.devlab.model.dto.user.UserQueryRequest;
import com.sheldon.devlab.model.entity.User;
import com.sheldon.devlab.model.enums.UserRoleEnum;
import com.sheldon.devlab.model.vo.LoginUserVO;
import com.sheldon.devlab.model.vo.UserVO;
import com.sheldon.devlab.service.UserService;
import com.sheldon.devlab.utils.AlgorithmUtils;
import com.sheldon.devlab.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.util.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.sheldon.devlab.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务实现
 *
 * @author <a href="https://github.com/sheldon-3601e">sheldon</a>
 * @from <a href="https://github.com/sheldon-3601e">sheldon</a>
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 盐值，混淆密码
     */
    public static final String SALT = "yupi";

    Gson gson = new Gson();


    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        synchronized (userAccount.intern()) {
            // 账户不能重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount", userAccount);
            long count = this.baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
            }
            // 2. 加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            // 3. 插入数据
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return user.getId();
        }
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 3. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        return this.getLoginUserVO(user);
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }

        return currentUser;
    }

    /**
     * 获取当前登录用户（允许未登录）
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUserPermitNull(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            return null;
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        return this.getById(userId);
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return isAdmin(user);
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    /**
     * 用户注销
     *
     * @param request
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        if (request.getSession().getAttribute(USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        loginUserVO.setTags(this.getTags(user));
        return loginUserVO;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setTags(this.getTags(user));
        return userVO;
    }

    @Override
    public List<UserVO> getUserVO(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String unionId = userQueryRequest.getUnionId();
        String mpOpenId = userQueryRequest.getMpOpenId();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(unionId), "unionId", unionId);
        queryWrapper.eq(StringUtils.isNotBlank(mpOpenId), "mpOpenId", mpOpenId);
        queryWrapper.eq(StringUtils.isNotBlank(userRole), "userRole", userRole);
        queryWrapper.like(StringUtils.isNotBlank(userProfile), "userProfile", userProfile);
        queryWrapper.like(StringUtils.isNotBlank(userName), "userName", userName);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 根据标签查询用户（SQL语句）
     *
     * @param tagNameList 标签列表
     * @return
     */
    @Override
    public List<UserVO> searchUserByTagsUseSQL(List<String> tagNameList) {
        if (CollUtil.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标签为空");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        for (String tagName : tagNameList) {
            queryWrapper = queryWrapper.like("tags", tagName);
        }
        List<User> users = userMapper.selectList(queryWrapper);

        return this.getUserVO(users);
    }

    /**
     * 根据标签查询用户（内存）
     *
     * @param tagNameList 标签列表
     * @return
     */
    @Override
    public List<UserVO> searchUserByTagsUseMemory(List<String> tagNameList) {
        if (CollUtil.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标签为空");
        }
        // 1.先查询出所有的用户
        List<User> allUsers = userMapper.selectList(null);
        // 2.遍历用户
        List<User> userList = allUsers.stream().filter(user -> {
            // 3.解析标签列表，转化为字符串集合
            Set<String> tagNameSet = gson.fromJson(user.getTags(), new TypeToken<Set<String>>() {
            }.getType());
            if (CollUtil.isEmpty(tagNameSet)) {
                return false;
            }
            // 4.判断是否包含标签
            return tagNameSet.containsAll(tagNameList);
        }).collect(Collectors.toList());

        return this.getUserVO(userList);
    }

    @Override
    public List<User> filtersUsersByTag(List<User> userList, List<String> tagNameList) {
        if (CollUtil.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标签为空");
        }
        // 2.遍历用户
        List<User> userRes = userList.stream().filter(user -> {
            // 3.解析标签列表，转化为字符串集合
            Set<String> tagNameSet = gson.fromJson(user.getTags(), new TypeToken<Set<String>>() {
            }.getType());
            if (CollUtil.isEmpty(tagNameSet)) {
                return false;
            }
            // 4.判断是否包含标签
            return tagNameSet.containsAll(tagNameList);
        }).collect(Collectors.toList());

        return userRes;
    }

    @Override
    public List<String> getTags(User user) {
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户为空");
        }
        String tags = user.getTags();
        if (StringUtils.isBlank(tags)) {
            return new ArrayList<>();
        }
        return gson.fromJson(tags, new TypeToken<List<String>>() {
        }.getType());
    }

    @Override
    public List<String> getTags(String tags) {
        if (StringUtils.isBlank(tags)) {
            return new ArrayList<>();
        }
        return gson.fromJson(tags, new TypeToken<List<String>>() {
        }.getType());
    }

    @Override
    public List<UserVO> getRecommendUserList() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // id 小于等于 32
        queryWrapper.le("id", 32);
        List<User> users = userMapper.selectList(queryWrapper);
        return this.getUserVO(users);
    }

    @Override
    public List<UserVO> listMatchUSerVO(UserMatchQueryRequest userMatchQueryRequest, User loginUser) {

        Integer matchNum = userMatchQueryRequest.getMatchNum();
        if (matchNum == null || matchNum <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "匹配数量错误");
        }
        List<String> tagList = this.getTags(loginUser);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "tags");
        queryWrapper.isNotNull("tags");
        List<User> userList = this.list(queryWrapper);
        // 用户 => 相似度
        List<Pair<User, Long>> userDistanceList = new ArrayList<>();
        for (User user : userList) {
            String userTags = user.getTags();
            if (StringUtils.isBlank(userTags) || user.getId() == loginUser.getId()) {
                continue;
            }
            List<String> userTagList = this.getTags(userTags);
            long distance = AlgorithmUtils.minDistance(tagList, userTagList);
            userDistanceList.add(new Pair<>(user, distance));
        }
        // 按照相似度升序排列
        List<Pair<User, Long>> topUserPairList = userDistanceList
                .stream()
                .sorted((a, b) -> (int) (a.getValue() - b.getValue()))
                .limit(matchNum)
                .collect(Collectors.toList());

        // 获取用户 id 列表
        List<Long> topUserIdList = topUserPairList
                .stream()
                .map(pair -> pair.getKey().getId())
                .collect(Collectors.toList());

        if (CollUtil.isEmpty(topUserIdList)) {
            return new ArrayList<>();
        }
        QueryWrapper<User> topUserQueryWrapper = new QueryWrapper<>();
        topUserQueryWrapper.in("id", topUserIdList);
        // 未排序的用户 Map 集合
        // id -> UserVO
        Map<Long, List<UserVO>> unOrderUserList = this.list(topUserQueryWrapper).stream().map(this::getUserVO).collect(Collectors.groupingBy(UserVO::getId));

        // 根据排序的顺序，封装返回的用户列表
        List<UserVO> resultUserVOList = new ArrayList<>();
        topUserIdList.forEach(id -> {
            List<UserVO> userVOList = unOrderUserList.get(id);
            if (CollUtil.isNotEmpty(userVOList)) {
                resultUserVOList.add(userVOList.get(0));
            }
        });

        return resultUserVOList;
    }

    /**
     * JSON_CONTAINS(tags, JSON_ARRAY(...))：这是一个 MySQL 的 JSON 函数，用于判断一个 JSON 数组中是否包含某个值。<p>
     * 在这里，我们使用它来判断 tags 字段中是否包含传入的标签列表中的任何一个标签。
     * <p>
     * normalizedTagNames.stream().map(tag -> "'" + tag + "'").collect(Collectors.joining(", "))：<p>
     * 这段代码用于将标签列表中的每个标签都用单引号括起来，并使用逗号连接成一个字符串。例如，如果标签列表是 ["tag1", "tag2", "tag3"]，则这段代码将生成 'tag1', 'tag2', 'tag3'。
     * <p>
     * 综合起来，这段代码的作用是构建一个 SQL 条件，用于判断 tags 字段是否包含传入的标签列表中的任何一个标签。<p>
     * 最终生成的 SQL 片段类似于 JSON_CONTAINS(tags, JSON_ARRAY('tag1', 'tag2', 'tag3'))。
     * <p>
     * 这个 SQL 条件将被添加到 QueryWrapper 中，作为查询条件的一部分，在执行查询时会被转换为相应的 SQL 语句，从而实现对标签字段的过滤。<p>
     * <p>
     * 总之，这段代码的作用是在 MyBatis-Plus 的查询中添加一个自定义的 SQL 条件，用于对标签字段进行过滤，以满足复杂的查询需求。<p>
     * @param userQueryByTagRequest
     * @return
     */
    @Override
    public Page<UserVO> listUserVOByTagAndPage(UserQueryByTagRequest userQueryByTagRequest) {

        int current = userQueryByTagRequest.getCurrent();
        int size = userQueryByTagRequest.getPageSize();
        String searchKey = userQueryByTagRequest.getSearchKey();
        List<String> tagNameList = userQueryByTagRequest.getTagNameList();

        // 构建查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(searchKey)) {
            queryWrapper.and(qw -> qw.like("userName", searchKey).or().like("userProfile", searchKey));
        }
        if (!CollectionUtils.isEmpty(tagNameList)) {
            queryWrapper.apply("(tags IS NOT NULL AND JSON_CONTAINS(tags, JSON_ARRAY(" + tagNameList.stream().map(tag -> "'" + tag + "'").collect(Collectors.joining(", ")) + ")))");
        }

        Page<User> userPage = this.page(new Page<>(current, size),
                queryWrapper);

        List<User> userList = userPage.getRecords();
        long total = userPage.getTotal() > 10 ? 10 : userPage.getTotal();
        // 封装返回结果
        Page<UserVO> userVOPage = new Page<>(current, size, total);
        List<UserVO> userVO = this.getUserVO(userList);
        userVOPage.setRecords(userVO);

        return userVOPage;
    }

}
