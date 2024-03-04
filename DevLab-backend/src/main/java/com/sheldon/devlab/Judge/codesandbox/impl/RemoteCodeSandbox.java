package com.sheldon.devlab.Judge.codesandbox.impl;

import com.sheldon.devlab.Judge.codesandbox.CodeSandbox;
import com.sheldon.devlab.Judge.codesandbox.model.ExecuteCodeRequest;
import com.sheldon.devlab.Judge.codesandbox.model.ExecuteCodeResponse;

/**
 * @ClassName RemoteCodeSandbox
 * @Author sheldon
 * @Date 2024/3/4 16:01
 * @Version 1.0
 * @Description 远程代码沙箱（实际请求业务）
 */
public class RemoteCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("");
        return null;
    }
}
