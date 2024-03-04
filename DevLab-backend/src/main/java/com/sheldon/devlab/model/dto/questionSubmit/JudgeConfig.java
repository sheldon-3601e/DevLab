package com.sheldon.devlab.model.dto.questionSubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName JudgeCase
 * @Author sheldon
 * @Date 2024/3/2 15:44
 * @Version 1.0
 * @Description 题目判题配置
 */
@Data
public class JudgeConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 时间限制（ms）
     */
    private long timeLimit;

    /**
     * 内存限制（kb）
     */
    private long memoryLimit;

    /**
     * 栈限制
     */
    private long stackLimit;

}
