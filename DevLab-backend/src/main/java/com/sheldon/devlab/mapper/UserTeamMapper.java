package com.sheldon.devlab.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sheldon.devlab.model.entity.UserTeam;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author sheldon
* @description 针对表【user_team(用户队伍关系)】的数据库操作Mapper
* @createDate 2024-02-20 16:26:45
* @Entity com.sheldon.devlab.model.entity.UserTeam
*/
public interface UserTeamMapper extends BaseMapper<UserTeam> {

    /**
     * 查询用户加入的队伍id
     *
     * @param userId
     * @return
     */
    @Select("select teamId from user_team where userId = #{userId} and isDelete = 0")
    List<Long> listHasJoinTeamId(Long userId);
}




