package com.sheldon.devlab.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sheldon.devlab.common.DeleteRequest;
import com.sheldon.devlab.common.ErrorCode;
import com.sheldon.devlab.exception.BusinessException;
import com.sheldon.devlab.exception.ThrowUtils;
import com.sheldon.devlab.mapper.UserTeamMapper;
import com.sheldon.devlab.model.dto.team.TeamJoinRequest;
import com.sheldon.devlab.model.dto.team.TeamQuitRequest;
import com.sheldon.devlab.model.entity.UserTeam;
import com.sheldon.devlab.model.enums.TeamStatusEnum;
import com.sheldon.devlab.model.enums.UserRoleEnum;
import com.sheldon.devlab.service.TeamService;
import com.sheldon.devlab.service.UserService;
import com.sheldon.devlab.mapper.TeamMapper;
import com.sheldon.devlab.model.dto.team.TeamQueryRequest;
import com.sheldon.devlab.model.entity.Team;
import com.sheldon.devlab.model.entity.User;
import com.sheldon.devlab.model.vo.TeamUserVO;
import com.sheldon.devlab.model.vo.UserVO;
import com.sheldon.devlab.service.UserTeamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sheldon
 * @description 针对表【team(队伍)】的数据库操作Service实现
 * @createDate 2024-02-20 16:26:45
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
        implements TeamService {

    /**
     * 盐值，混淆密码
     */
    public static final String SALT = "sheldon-devlab-2024-02-20-16-26-45";

    @Resource
    private UserTeamService userTeamService;

    @Resource
    private UserService userService;

    @Resource
    private UserTeamMapper userTeamMapper;

    @Override
    @Transactional
    public Long addTeam(Team team, User loginUser) {
        // 校验参数是否正确
        // 1. 队伍名称不能为空且长度小于等于20
        String teamName = team.getTeamName();
        if (StringUtils.isEmpty(teamName) || teamName.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 队伍描述不能为空且长度小于等于200
        String description = team.getDescription();
        if (StringUtils.isEmpty(description) || description.length() > 200) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 最大人数大于零且小于等于10
        int maxNum = Optional.ofNullable(team.getMaxNum()).orElse(5);
        if (maxNum <= 0 || maxNum > 10) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 过期时间要大于当前时间
        Date expireTime = team.getExpireTime();
        if (expireTime != null && expireTime.before(new Date())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 队伍状态只能是0, 1, 2
        // 如果是加密状态，密码不能为空
        int status = Optional.ofNullable(team.getStatus()).orElse(0);
        // TODO 提取公共的方法
        TeamStatusEnum enumValue = TeamStatusEnum.getEnumByValue(status);
        if (enumValue == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (TeamStatusEnum.SECRET.equals(enumValue)) {
            String password = team.getPassword();
            if (StringUtils.isBlank(password) || password.length() > 32) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            } else {
                // 对密码进行加密
                String newPassword = password + SALT;
                String md5Hex1 = DigestUtil.md5Hex(newPassword);
                team.setPassword(md5Hex1);
            }
        }
        // 如果是公开状态，密码置空
        if (TeamStatusEnum.PUBLIC.equals(enumValue)) {
            team.setPassword(null);
        }

        Long userId = loginUser.getId();
        team.setUserId(userId);

        // 校验用户最多只能创建五个队伍
        // TODO 用户可能一瞬间创建多个队伍，需要加锁
        // 思路：在用户表中加入一个字段，记录用户创建队伍的时间，如果在一分钟内创建多个队伍，就加锁
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        long count = this.count(queryWrapper);
        if (count >= 5) {
            throw new BusinessException(ErrorCode.MAX_TEAM_NUM_ERROR);
        }

        // 插入信息到队伍表
        boolean save = this.save(team);
        ThrowUtils.throwIf(!save, ErrorCode.SYSTEM_ERROR);
        // 插入信息到用户队伍关系表
        Long teamId = team.getId();
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        save = userTeamService.save(userTeam);
        ThrowUtils.throwIf(!save, ErrorCode.SYSTEM_ERROR);
        return teamId;

    }

    @Override
    @Transactional
    public boolean deleteTeam(DeleteRequest deleteRequest, User loginUser) {

        Long teamId = deleteRequest.getId();
        // 只有管理员和创建者可以删除队伍
        isAdminOrCreator(teamId, loginUser);
        // 删除队伍
        boolean remove = this.removeById(teamId);
        ThrowUtils.throwIf(!remove, ErrorCode.SYSTEM_ERROR);
        // 删除用户队关系表
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teamId", teamId);
        remove = userTeamService.remove(queryWrapper);
        ThrowUtils.throwIf(!remove, ErrorCode.SYSTEM_ERROR);
        return true;
    }

    @Override
    public boolean updateTeam(Team team, User loginUser) {
        // 判断是否有权限
        Long teamId = team.getId();
        isAdminOrCreator(teamId, loginUser);

        // 判断参数是否正确
        // 1. 队伍名称不能为空且长度小于等于20
        String teamName = team.getTeamName();
        if (!StringUtils.isEmpty(teamName)) {
            ThrowUtils.throwIf(teamName.length() > 20, ErrorCode.PARAMS_ERROR);
        }
        // 队伍描述不能为空且长度小于等于200
        String description = team.getDescription();
        if (!StringUtils.isEmpty(description)) {
            ThrowUtils.throwIf(description.length() > 200, ErrorCode.PARAMS_ERROR);
        }
        // 过期时间要大于当前时间
        Date expireTime = team.getExpireTime();
        if (expireTime != null) {
            ThrowUtils.throwIf(expireTime.before(new Date()), ErrorCode.PARAMS_ERROR);
        }
        // 队伍状态只能是0, 1, 2
        Integer status = team.getStatus();
        if (status != null) {
            TeamStatusEnum enumValue = TeamStatusEnum.getEnumByValue(status);
            if (enumValue == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            // 如果是加密状态，密码不能为空
            if (TeamStatusEnum.SECRET.equals(enumValue)) {
                String password = team.getPassword();
                if (StringUtils.isBlank(password) || password.length() > 32) {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR);
                } else {
                    // 对密码进行加密
                    String newPassword = password + SALT;
                    String md5Hex1 = DigestUtil.md5Hex(newPassword);
                    team.setPassword(md5Hex1);
                }
            }
            // 如果是公开状态，密码置空
            if (TeamStatusEnum.PUBLIC.equals(enumValue)) {
                team.setPassword(null);
            }
        }

        // 更新队伍
        boolean update = this.updateById(team);
        ThrowUtils.throwIf(!update, ErrorCode.SYSTEM_ERROR);
        return update;
    }

    @Override
    public boolean isAdminOrCreator(Long teamId, User loginUser) {
        // 如果是管理员，直接返回true
        if (UserRoleEnum.ADMIN.getValue().equals(loginUser.getUserRole())) {
            return true;
        }
        // 判断是否为创建者
        Team team = this.getById(teamId);
        if (team == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (team.getUserId().equals(loginUser.getId())) {
            return true;
        }
        throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
    }

    @Override
    public void isAdminOrCreator(Team team, User loginUser) {
        // 如果是管理员，直接返回true
        if (!UserRoleEnum.ADMIN.getValue().equals(loginUser.getUserRole())) {
            // 判断是否为创建者
            if (!team.getUserId().equals(loginUser.getId())) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
        }
    }

    @Override
    public Page<TeamUserVO> listTeamUserVOByPage(TeamQueryRequest teamQueryRequest, User loginUser) {
        String searchKey = teamQueryRequest.getSearchKey();
        String teamName = teamQueryRequest.getTeamName();
        String description = teamQueryRequest.getDescription();
        Integer maxNum = teamQueryRequest.getMaxNum();
        Integer status = teamQueryRequest.getStatus();
        int current = teamQueryRequest.getCurrent();
        int pageSize = teamQueryRequest.getPageSize();
        String sortField = teamQueryRequest.getSortField();
        String sortOrder = teamQueryRequest.getSortOrder();
        // 组装查询条件
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(sortField) && StringUtils.isNotBlank(sortOrder)) {
            queryWrapper.orderBy(true, "ascend".equals(sortOrder), sortField);
        }
        if (StringUtils.isNotBlank(searchKey)) {
            queryWrapper.and(wrapper -> wrapper.like("teamName", searchKey)
                    .or().like("description", searchKey));
        }
        if (StringUtils.isNotBlank(teamName)) {
            queryWrapper.like("teamName", teamName);
        }
        if (StringUtils.isNotBlank(description)) {
            queryWrapper.like("description", description);
        }
        if (maxNum != null) {
            queryWrapper.eq("maxNum", maxNum);
        }
        // 查询不存在过期时间或者过期时间大于当前时间
        queryWrapper.and(wrapper ->
                wrapper.isNull("expireTime").or().ge("expireTime", new Date()));
        // 普通用户只允许查询公开和加密的队伍
        // 管理员允许查询所有队伍
        if (status == null) {
            queryWrapper.in("status", TeamStatusEnum.PUBLIC.getValue(), TeamStatusEnum.SECRET.getValue());
        } else {
            TeamStatusEnum enumByValue = TeamStatusEnum.getEnumByValue(status);
            if (enumByValue != null) {
                if (!UserRoleEnum.ADMIN.getValue().equals(loginUser.getUserRole()) && (TeamStatusEnum.PRIVATE.equals(enumByValue))) {
                    throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
                }
                queryWrapper.eq("status", status);
            }
        }

        // 分页查询队伍列表
        Page<Team> teamPage = this.page(new Page<>(current, pageSize), queryWrapper);

        // 取出队伍列表
        List<Team> teamList = teamPage.getRecords();
        // 除去本身已经加入的队伍
        Long userId = loginUser.getId();

        if (teamList != null && !teamList.isEmpty()) {
            // 查询已加入的队伍 id
            List<Long> hasJoinTeamIdList = userTeamMapper.listHasJoinTeamId(userId);
            // 过滤掉已加入的队伍
            teamList = teamList.stream().
                    filter(team -> !hasJoinTeamIdList.contains(team.getId())).
                    collect(Collectors.toList());
        }
        // 根据创建者id取出创建者信息
        List<TeamUserVO> teamUserVOList = getTeamUserVO(teamList);

        // 封装成Page返回
        Page<TeamUserVO> teamUserVOPage = new Page<>();
        teamUserVOPage.setCurrent(teamPage.getCurrent());
        teamUserVOPage.setSize(teamPage.getSize());
        teamUserVOPage.setTotal(teamUserVOList.size());
        teamUserVOPage.setRecords(teamUserVOList);
        return teamUserVOPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser) {
        if (teamJoinRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long userId = loginUser.getId();
        Long teamId = teamJoinRequest.getTeamId();
        // 获取队伍信息
        Team team = this.getTeamById(teamId);

        Date expireTime = team.getExpireTime();
        Integer status = team.getStatus();
        String password = team.getPassword();
        Integer maxNum = team.getMaxNum();
        Integer hasNum = team.getHasNum();
        // 只能加入未满、未过期的队伍
        if (hasNum >= maxNum) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍已满");
        }
        if (expireTime != null && expireTime.before(new Date())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "房间已过期");
        }
        // 不能加入私密房间
        TeamStatusEnum teamStatusEnum = TeamStatusEnum.getEnumByValue(status);
        if (TeamStatusEnum.PRIVATE.equals(teamStatusEnum)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能加入私密房间");
        }
        // 如果加入加密房间，必须密码匹配
        String joinPassword = teamJoinRequest.getPassword();
        if (TeamStatusEnum.SECRET.equals(teamStatusEnum)) {
            if (StringUtils.isBlank(joinPassword)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码不能为空");
            }
            String newPassword = joinPassword + SALT;
            String md5Hex1 = DigestUtil.md5Hex(newPassword);
            if (!md5Hex1.equals(password)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "房间密码错误");
            }
        }
        // 用户最多加入五个队伍
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        long userHasJoinTeamNum = userTeamService.count(queryWrapper);
        if (userHasJoinTeamNum >= 5) {
            throw new BusinessException(ErrorCode.MAX_TEAM_NUM_ERROR);
        }
        // 不能重复加入
        queryWrapper.eq("teamId", teamId);
        long isHasJoin = userTeamService.count(queryWrapper);
        if (isHasJoin > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能重复加入");
        }
        // 修改队伍已加入人数
        team.setHasNum(hasNum + 1);
        boolean update = this.updateById(team);
        ThrowUtils.throwIf(!update, ErrorCode.SYSTEM_ERROR);
        // 新增队伍和用户的关联信息
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        return userTeamService.save(userTeam);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser) {

        // 校验请求参数
        if (teamQuitRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 校验队伍是否存在
        Long teamId = teamQuitRequest.getId();
        Long userId = loginUser.getId();
        Team team = this.getTeamById(teamId);

        // 校验是否加入队伍
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teamId", teamId);
        queryWrapper.eq("userId", userId);
        long count = userTeamService.count(queryWrapper);
        if (count == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "未加入队伍");
        }
        // 如果队伍只剩一人，直接解散队伍，并删除用户队伍的关联信息
        Integer hasNum = team.getHasNum();
        if (hasNum == 1) {
            boolean result = this.removeById(teamId);
            ThrowUtils.throwIf(!result, ErrorCode.SYSTEM_ERROR);
        } else {
            // 说明队伍中，至少有两个人
            Long creatorId = team.getUserId();
            if (creatorId.equals(userId)) {
                // 如果是队长
                // 权限转移给第二早加入的用户
                QueryWrapper<UserTeam> queryLeader = new QueryWrapper<>();
                queryLeader.eq("teamId", teamId);
                queryLeader.gt("userId", creatorId);
                queryLeader.orderByAsc("joinTime");
                queryLeader.last("limit 1");
                UserTeam newTeamLeader = userTeamService.getOne(queryLeader);
                // 更改队伍创建者
                team.setUserId(newTeamLeader.getUserId());
            }
            // 如果不是队长
            // 修改队伍已加入人数
            team.setHasNum(hasNum - 1);
            boolean result = this.updateById(team);
            ThrowUtils.throwIf(!result, ErrorCode.SYSTEM_ERROR);
        }

        // 删除用户队伍关系表
        boolean result = userTeamService.remove(queryWrapper);
        ThrowUtils.throwIf(!result, ErrorCode.SYSTEM_ERROR);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean dissolveTeam(DeleteRequest deleteRequest, User loginUser) {
        if (deleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long userId = loginUser.getId();
        Long teamId = deleteRequest.getId();
        Team team = this.getTeamById(teamId);
        // 判断是否为创建者
        if (!team.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "不是队伍创建者");
        }
        // 删除队伍
        boolean result = this.removeById(teamId);
        ThrowUtils.throwIf(!result, ErrorCode.SYSTEM_ERROR);
        // 删除用户队伍关联信息
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teamId", teamId);
        result = userTeamService.remove(queryWrapper);
        ThrowUtils.throwIf(!result, ErrorCode.SYSTEM_ERROR);
        return true;
    }

    @Override
    public List<TeamUserVO> listJoinedTeam(User loginUser) {
        Long userId = loginUser.getId();
        // 查询已加入的队伍 id
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        List<UserTeam> userTeamList = userTeamService.list(queryWrapper);
        // 根据队伍 id 查询队伍信息
        // 过滤到可能重复的队伍 Id
        Map<Long, List<UserTeam>> listMap = userTeamList.stream().collect(Collectors.groupingBy(UserTeam::getTeamId));
        ArrayList<Long> idList = new ArrayList<>(listMap.keySet());
        if (idList.isEmpty()) {
            return Collections.emptyList();
        }
        List<Team> teams = this.listByIds(idList);
        // 再查询 队伍的创建者信息, 封装返回对象
        return getTeamUserVO(teams);
    }

    @Override
    public List<TeamUserVO> listCreatedTeam(User loginUser) {
        Long userId = loginUser.getId();
        // 查询出创建的队伍
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        List<Team> teamList = this.list(queryWrapper);
        if (teamList == null || teamList.isEmpty()) {
            return Collections.emptyList();
        }
        // 封装返回对象，将 userVO 封装
        return teamList.stream().map(team -> {
            TeamUserVO teamUserVO = new TeamUserVO();
            BeanUtils.copyProperties(team, teamUserVO);
            UserVO userVO = userService.getUserVO(loginUser);
            teamUserVO.setCreateUser(userVO);
            return teamUserVO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<TeamUserVO> getTeamUserVO(List<Team> teams) {
        if (teams == null || teams.isEmpty()) {
            return Collections.emptyList();
        }
        return teams.stream().map(team -> {
            TeamUserVO teamUserVO = new TeamUserVO();
            BeanUtils.copyProperties(team, teamUserVO);
            User user = userService.getById(team.getUserId());
            UserVO userVO = userService.getUserVO(user);
            teamUserVO.setCreateUser(userVO);
            return teamUserVO;
        }).collect(Collectors.toList());
    }

    /**
     * 根据 Id 获取队伍信息
     *
     * @param teamId
     * @return
     */
    private Team getTeamById(Long teamId) {
        if (teamId == null || teamId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = this.getById(teamId);
        if (team == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "队伍不存在");
        }
        return team;
    }

}




