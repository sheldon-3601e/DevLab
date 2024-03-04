package com.sheldon.devlab.Judge.strategy;

import com.sheldon.devlab.model.dto.question.JudgeInfo;
import com.sheldon.devlab.model.dto.questionSubmit.JudgeConfig;
import com.sheldon.devlab.model.enums.JudgeInfoMessageEnum;

import java.util.List;

/**
 * @ClassName DefaultJudgeStrategy
 * @Author sheldon
 * @Date 2024/3/4 18:23
 * @Version 1.0
 * @Description 默认策略模式实现
 */
public class DefaultJudgeStrategy implements JudgeStrategy {
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        List<String> outputList = judgeContext.getOutputList();
        List<String> responseOutputList = judgeContext.getResponseOutputList();
        JudgeConfig judgeConfig = judgeContext.getJudgeConfig();
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();

        long timeLimit = judgeConfig.getTimeLimit();
        long memoryLimit = judgeConfig.getMemoryLimit();

        Long memory = judgeInfo.getMemory();
        long time = judgeInfo.getTime();

        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.Accepted;
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        judgeInfoResponse.setMemory(memory);
        judgeInfoResponse.setTime(time);

        if (outputList.size() != responseOutputList.size()) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        for (int i = 0; i < outputList.size(); i++) {
            if (!outputList.get(i).equals(responseOutputList.get(i))) {
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
                return judgeInfoResponse;
            }
        }
        if (time > timeLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        if (memory > memoryLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
        return judgeInfoResponse;
    }
}
