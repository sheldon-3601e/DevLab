package com.sheldon.devlab.Judge.codesandbox;

import com.sheldon.devlab.Judge.codesandbox.model.ExecuteCodeRequest;
import com.sheldon.devlab.Judge.codesandbox.model.ExecuteCodeResponse;
import com.sheldon.devlab.model.enums.QuestionSubmitLanguageEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @ClassName CodeSandboxProxyTest
 * @Author sheldon
 * @Date 2024/3/4 16:28
 * @Version 1.0
 * @Description TODO
 */
@SpringBootTest
class CodeSandboxProxyTest {

    @Value("${codesandbox.type:example}")
    private String type;

    @Test
    void executeCode() {
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        CodeSandboxProxy codeSandboxProxy = new CodeSandboxProxy(codeSandbox);
        String code = "int main() { }";
        String language = QuestionSubmitLanguageEnum.JAVA.getValue();
        List<String> inputList = Arrays.asList("1 2", "3 4");
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandboxProxy.executeCode(executeCodeRequest);
    }
}