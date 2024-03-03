package com.sheldon.devlab.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sheldon.devlab.backend.annotation.AuthCheck;
import com.sheldon.devlab.backend.common.BaseResponse;
import com.sheldon.devlab.backend.common.DeleteRequest;
import com.sheldon.devlab.backend.common.ErrorCode;
import com.sheldon.devlab.backend.common.ResultUtils;
import com.sheldon.devlab.backend.constant.RedisConstant;
import com.sheldon.devlab.backend.exception.BusinessException;
import com.sheldon.devlab.backend.exception.ThrowUtils;
import com.sheldon.devlab.backend.model.dto.team.*;
import com.sheldon.devlab.backend.model.dto.team.*;
import com.sheldon.devlab.backend.model.entity.Team;
import com.sheldon.devlab.backend.model.entity.User;
import com.sheldon.devlab.backend.model.vo.TeamUserVO;
import com.sheldon.devlab.backend.service.TeamService;
import com.sheldon.devlab.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 队伍接口
 *
 * @author <a href="https://github.com/sheldon-3601e">sheldon</a>
 * @from <a href="https://github.com/sheldon-3601e">sheldon</a>
 */
@RestController
@RequestMapping("/team")
@Slf4j
public class TeamController {

    @Resource
    private UserService userService;

    @Resource
    private TeamService teamService;

    @Resource
    private RedissonClient redissonClient;

    // region 增删改查

    /**
     * 创建队伍
     *
     * @param teamAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request) {

        if (teamAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Team team = new Team();
        BeanUtils.copyProperties(teamAddRequest, team);
        Long teamId = teamService.addTeam(team, loginUser);

        return ResultUtils.success(teamId);
    }

    /**
     * 删除队伍
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 只有管理员和创建者可以删除队伍
        User loginUser = userService.getLoginUser(request);
        boolean b = teamService.deleteTeam(deleteRequest, loginUser);
        return ResultUtils.success(b);
    }

    /**
     * 更新队伍
     *
     * @param teamUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest,
                                            HttpServletRequest request) {
        if (teamUpdateRequest == null || teamUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();
        BeanUtils.copyProperties(teamUpdateRequest, team);
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.updateTeam(team, loginUser);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 分页获取队伍封装列表
     *
     * @param teamQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    @AuthCheck(mustLogin = true)
    public BaseResponse<Page<TeamUserVO>> listTeamUserVOByPage(@RequestBody TeamQueryRequest teamQueryRequest,
                                                               HttpServletRequest request) {
        if (teamQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        long size = teamQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        Page<TeamUserVO> teamUserVOPage = teamService.listTeamUserVOByPage(teamQueryRequest, loginUser);
        return ResultUtils.success(teamUserVOPage);
    }

    /**
     * 用户加入队伍
     *
     * @param teamJoinRequest
     * @param request
     * @return
     */
    @PostMapping("/join")
    public BaseResponse<Boolean> joinTeam(@RequestBody TeamJoinRequest teamJoinRequest,
                                          HttpServletRequest request) {
        if (teamJoinRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        // 限流
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(RedisConstant.JOIN_TEAM_LOCK + userId);
        rateLimiter.trySetRate(RateType.OVERALL, 1, 1, RateIntervalUnit.SECONDS);

        boolean res = rateLimiter.tryAcquire();
        boolean result = false;
        if (res) {
            // 获取令牌成功
            result = teamService.joinTeam(teamJoinRequest, loginUser);
        }
        return ResultUtils.success(result);
    }

    /**
     * 用户退出队伍
     *
     * @param teamQuitRequest
     * @param request
     * @return
     */
    @PostMapping("/quit")
    public BaseResponse<Boolean> quitTeam(@RequestBody TeamQuitRequest teamQuitRequest,
                                          HttpServletRequest request) {
        if (teamQuitRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.quitTeam(teamQuitRequest, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 解散队伍
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/dissolve")
    public BaseResponse<Boolean> dissolveTeam(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 只有管理员和创建者可以删除队伍
        User loginUser = userService.getLoginUser(request);
        boolean b = teamService.dissolveTeam(deleteRequest, loginUser);
        return ResultUtils.success(b);
    }

    /**
     * 查询已加入的队伍
     *
     * @param request
     * @return
     */
    @PostMapping("/list/my/joined")
    public BaseResponse<List<TeamUserVO>> listJoinedTeam(HttpServletRequest request) {

        User loginUser = userService.getLoginUser(request);
        List<TeamUserVO> joinedTeamList = teamService.listJoinedTeam(loginUser);
        return ResultUtils.success(joinedTeamList);
    }

    /**
     * 查询已创建的队伍
     *
     * @param request
     * @return
     */
    @PostMapping("/list/my/create")
    public BaseResponse<List<TeamUserVO>> listCreatedTeam(HttpServletRequest request) {

        User loginUser = userService.getLoginUser(request);
        List<TeamUserVO> joinedTeamList = teamService.listCreatedTeam(loginUser);
        return ResultUtils.success(joinedTeamList);
    }

}
