package com.sheldon.devlab.Judge.codesandbox.impl;

import com.sheldon.devlab.Judge.codesandbox.CodeSandbox;
import com.sheldon.devlab.Judge.codesandbox.model.ExecuteCodeRequest;
import com.sheldon.devlab.Judge.codesandbox.model.ExecuteCodeResponse;
import com.sheldon.devlab.model.dto.question.JudgeInfo;
import com.sheldon.devlab.model.enums.JudgeInfoMessageEnum;
import com.sheldon.devlab.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * @ClassName ExampleCodeSandbox
 * @Author sheldon
 * @Date 2024/3/4 16:00
 * @Version 1.0
 * @Description 示例代码沙箱（跑通业务流程）
 */
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试代码沙箱信息");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.Accepted.getText());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
