package com.sheldon.devlab.Judge.codesandbox.impl;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.sheldon.devlab.Judge.codesandbox.CodeSandbox;
import com.sheldon.devlab.Judge.codesandbox.model.ExecuteCodeRequest;
import com.sheldon.devlab.Judge.codesandbox.model.ExecuteCodeResponse;
import com.sheldon.devlab.model.enums.QuestionSubmitStatusEnum;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.auxiliary.AuxiliaryType;

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
        String url = "http://localhost:8090/executeCode";
        // 发送请求
//        String body = HttpUtil.createPost(url)
//                .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_STR)
//                .body(JSONUtil.toJsonStr(executeCodeRequest))
//                .execute()
//                .body();
//        return JSONUtil.toBean(body, ExecuteCodeResponse.class);
        HttpResponse response = HttpUtil.createPost(url)
                .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_STR)
                .body(JSONUtil.toJsonStr(executeCodeRequest))
                .execute();
        if (response.getStatus() != 200) {
            log.error("remote code sandbox error: " + response.getStatus() + ' ' + response.body());
            ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
            executeCodeResponse.setMessage(response.body());
            executeCodeResponse.setStatus(QuestionSubmitStatusEnum.FAILED.getValue());
            return executeCodeResponse;
        }
        return JSONUtil.toBean(response.body(), ExecuteCodeResponse.class);
    }
}
