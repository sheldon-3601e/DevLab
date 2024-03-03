package com.sheldon.devlab.model.dto.team;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName TeamJoinRequest
 * @Author 26483
 * @Date 2024/2/22 7:21
 * @Version 1.0
 * @Description 用户退出队伍请求
 */
@Data
public class TeamQuitRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 队伍id
     */
    private Long id;

}
