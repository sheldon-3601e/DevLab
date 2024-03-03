package com.sheldon.devlab.backend.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName TeamWithUserVO
 * @Author 26483
 * @Date 2024/2/20 18:39
 * @Version 1.0
 * @Description 队伍用户VO
 */
@Data
public class TeamUserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 队伍名称
     */
    private String teamName;

    /**
     * 描述
     */
    private String description;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 当前人数
     */
    private Integer hasNum;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 创建用户
     */
    private UserVO createUser;

    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 入队用户列表
     */
    private List<UserVO> userList;

}
