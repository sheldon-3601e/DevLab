package com.sheldon.devlab.Judge.strategy;

import com.sheldon.devlab.model.dto.question.JudgeInfo;
import com.sheldon.devlab.model.dto.questionSubmit.JudgeConfig;
import com.sheldon.devlab.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * @ClassName JudgeContext
 * @Author sheldon
 * @Date 2024/3/4 18:22
 * @Version 1.0
 * @Description 策略模式上下文
 */
@Data
public class JudgeContext {

    private QuestionSubmit questionSubmit;

    private List<String> outputList;

    private List<String> responseOutputList;

    private JudgeConfig judgeConfig;

    private JudgeInfo judgeInfo;

}
