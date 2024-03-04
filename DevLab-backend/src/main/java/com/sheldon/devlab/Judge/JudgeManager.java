package com.sheldon.devlab.Judge;

import com.sheldon.devlab.Judge.strategy.DefaultJudgeStrategy;
import com.sheldon.devlab.Judge.strategy.JavaLangugeJudgeStrategy;
import com.sheldon.devlab.Judge.strategy.JudgeContext;
import com.sheldon.devlab.Judge.strategy.JudgeStrategy;
import com.sheldon.devlab.model.dto.question.JudgeInfo;
import com.sheldon.devlab.model.entity.QuestionSubmit;
import com.sheldon.devlab.model.enums.QuestionSubmitLanguageEnum;
import org.springframework.stereotype.Service;

/**
 * @ClassName JudgeManage
 * @Author sheldon
 * @Date 2024/3/4 18:35
 * @Version 1.0
 * @Description 管理判题策略
 */
@Service
public class JudgeManager {

    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();

        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if (QuestionSubmitLanguageEnum.JAVA.getValue() .equals(language)) {
            judgeStrategy = new JavaLangugeJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }



}
