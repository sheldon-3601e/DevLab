package com.sheldon.devlabcodesanbox.model;

import lombok.Data;

/**
 * @author sheldon
 * @version 1.0
 * @className ExecuteMessage
 * @date 2024/3/5 20:41
 * @description 进程执行信息封装类
 */
@Data
public class ExecuteMessage {

    private Integer exitValue;

    private String message;

    private String errorMessage;

    private Long time;

    private Long memory;

}
