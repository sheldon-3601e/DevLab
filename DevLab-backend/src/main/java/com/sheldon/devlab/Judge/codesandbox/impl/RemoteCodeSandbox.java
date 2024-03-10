package com.sheldon.devlab.Judge.codesandbox.impl;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.sheldon.devlab.Judge.codesandbox.CodeSandbox;
import com.sheldon.devlab.Judge.codesandbox.model.ExecuteCodeRequest;
import com.sheldon.devlab.Judge.codesandbox.model.ExecuteCodeResponse;
import com.sheldon.devlab.model.enums.QuestionSubmitStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName RemoteCodeSandbox
 * @Author sheldon
 * @Date 2024/3/4 16:01
 * @Version 1.0
 * @Description 远程代码沙箱（实际请求业务）
 */
@Slf4j
public class RemoteCodeSandbox implements CodeSandbox {

    private static final String AUTH_REQUEST_HEADER = "auth";

    private static final String AUTH_REQUEST_STR = "security";

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        String url = "http://39.96.116.124:8113/api/code/execute";
        // 发送请求
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();

        try {
            HttpResponse response = HttpUtil.createPost(url)
                    .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_STR)
                    .body(JSONUtil.toJsonStr(executeCodeRequest))
                    .execute();
            if (response.getStatus() != 200) {
                log.error("remote code sandbox error: " + response.getStatus() + ' ' + response.body());
                executeCodeResponse.setMessage(response.body());
                executeCodeResponse.setStatus(QuestionSubmitStatusEnum.FAILED.getValue());
            }
            executeCodeResponse = JSONUtil.toBean(response.body(), ExecuteCodeResponse.class);
        } catch (Exception e) {
            log.error("remote code sandbox error: " + e.getMessage());
            executeCodeResponse.setMessage(e.getMessage());
            executeCodeResponse.setStatus(QuestionSubmitStatusEnum.FAILED.getValue());
        } finally {
            return executeCodeResponse;
        }


    }
}
