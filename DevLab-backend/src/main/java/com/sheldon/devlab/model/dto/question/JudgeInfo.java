package com.sheldon.devlab.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName JudgeCase
 * @Author 26483
 * @Date 2024/3/2 15:44
 * @Version 1.0
 * @Description 题目判题用例
 */
@Data
public class JudgeInfo implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 程序执行信息
     */
    private String message;

    /**
     * 消耗内存
     */
    private String memory;

    /**
     * 消耗时间
     */
    private long time;

}
