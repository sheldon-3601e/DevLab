package com.sheldon.devlab.Judge.codesandbox.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName JudgeCase
 * @Author sheldon
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
    private Long memory;

    /**
     * 消耗时间
     */
    private long time;

}
