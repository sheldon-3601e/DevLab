package com.sheldon.devlab.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sheldon.devlab.common.DeleteRequest;
import com.sheldon.devlab.model.dto.team.TeamJoinRequest;
import com.sheldon.devlab.model.dto.team.TeamQueryRequest;
import com.sheldon.devlab.model.dto.team.TeamQuitRequest;
import com.sheldon.devlab.model.entity.Team;
import com.sheldon.devlab.model.entity.User;
import com.sheldon.devlab.model.vo.TeamUserVO;

import java.util.List;

/**
* @author sheldon
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2024-02-20 16:26:45
*/
public interface TeamService extends IService<Team> {

    /**
     * 创建队伍
     *
     * @param team
     * @param loginUser
     * @return
     */
    Long addTeam(Team team, User loginUser);

    /**
     * 删除队伍
     *
     * @param deleteRequest
     * @param loginUser
     * @return
     */
    boolean deleteTeam(DeleteRequest deleteRequest, User loginUser);

    /**
     * 更新队伍
     *
     * @param team
     * @param loginUser
     * @return
     */
    boolean updateTeam(Team team, User loginUser);

    /**
     * 校验是否为管理员或者队伍创建者
     *
     * @param teamId
     * @param loginUser
     * @return
     */
    boolean isAdminOrCreator(Long teamId, User loginUser);

    void isAdminOrCreator(Team team, User loginUser);

    /**
     * 分页查询队伍用户VO
     *
     * @param teamQueryRequest
     * @param loginUser
     * @return
     */
    Page<TeamUserVO> listTeamUserVOByPage(TeamQueryRequest teamQueryRequest, User loginUser);

    /**
     * 用户加入队伍
     * @param teamJoinRequest
     * @param loginUser
     * @return
     */
    boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);

    /**
     * 用户退出队伍
     * @param teamQuitRequest
     * @param loginUser
     * @return
     */
    boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);

    /**
     * 解散队伍
     * @param deleteRequest
     * @param loginUser
     * @return
     */
    boolean dissolveTeam(DeleteRequest deleteRequest, User loginUser);

    /**
     * 查询用户加入的队伍列表
     * @param loginUser
     * @return
     */
    List<TeamUserVO> listJoinedTeam(User loginUser);

    /**
     * 查询队伍用户VO
     * @param teams
     * @return
     */
    List<TeamUserVO> getTeamUserVO(List<Team> teams);

    /**
     * 查询用户创建的队伍列表
     * @param loginUser
     * @return
     */
    List<TeamUserVO> listCreatedTeam(User loginUser);


}
