package com.sheldon.devlab.backend.model.dto.user;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 用户更新个人信息请求
 *
 * @author <a href="https://github.com/sheldon-3601e">sheldon</a>
 * @from <a href="https://github.com/sheldon-3601e">sheldon</a>
 */
@Data
public class UserUpdateMyRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户性别
     */
    private String userGender;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 电话
     */
    private String userPhone;

    /**
     * 邮箱
     */
    private String userEmail;

    private static final long serialVersionUID = 1L;
}