package com.sheldon.devlab.model.dto.team;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName TeamJoinRequest
 * @Author sheldon
 * @Date 2024/2/22 7:21
 * @Version 1.0
 * @Description TODO
 */
@Data
public class TeamJoinRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 队伍id
     */
    private Long teamId;


    /**
     * 密码
     */
    private String password;

}
