package com.sheldon.devlab.backend.model.dto.questionSubmit;

import lombok.Data;

/**
 * @ClassName JudgeCase
 * @Author 26483
 * @Date 2024/3/2 15:44
 * @Version 1.0
 * @Description 题目判题配置
 */
@Data
public class JudgeConfig {

    /**
     * 时间限制（ms）
     */
    private long timeLimit;

    /**
     * 内存限制（kb）
     */
    private long MemoryLimit;

    /**
     * 栈限制
     */
    private long stackLimit;

}
