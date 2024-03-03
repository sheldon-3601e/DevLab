package com.sheldon.devlab.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sheldon.devlab.mapper.UserTeamMapper;
import com.sheldon.devlab.model.entity.UserTeam;
import com.sheldon.devlab.service.UserTeamService;
import org.springframework.stereotype.Service;

/**
* @author 26483
* @description 针对表【user_team(用户队伍关系)】的数据库操作Service实现
* @createDate 2024-02-20 16:26:45
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService {

}




