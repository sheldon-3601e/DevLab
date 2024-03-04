package com.sheldon.devlab.Judge.strategy;

import com.sheldon.devlab.model.dto.question.JudgeInfo;

/**
 * @ClassName JudgeStrategy
 * @Author sheldon
 * @Date 2024/3/4 18:21
 * @Version 1.0
 * @Description 策略模式接口
 */
public interface JudgeStrategy {

    JudgeInfo doJudge(JudgeContext judgeContext);

}
