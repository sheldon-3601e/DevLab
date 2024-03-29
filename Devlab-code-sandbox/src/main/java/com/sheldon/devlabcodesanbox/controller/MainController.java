package com.sheldon.devlabcodesanbox.controller;

import com.sheldon.devlabcodesanbox.model.ExecuteCodeRequest;
import com.sheldon.devlabcodesanbox.model.ExecuteCodeResponse;
import com.sheldon.devlabcodesanbox.template.JavaDockerCodeSandboxTemplateImpl;
import com.sheldon.devlabcodesanbox.template.JavaNativeCodeSandboxTemplateImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author sheldon
 * @version 1.0
 * @className MainController
 * @date 2024/3/5 18:43
 * @description 测试接口
 */
@RestController
@RequestMapping("/code")
public class MainController {

    private static final String AUTH_REQUEST_HEADER = "auth";

    private static final String AUTH_REQUEST_STR = "security";

    @Resource
    private JavaNativeCodeSandboxTemplateImpl javaNativeCodeSandboxTemplate;
    @Resource
    private JavaDockerCodeSandboxTemplateImpl javaDockerCodeSandboxTemplate;

    @PostMapping("/execute")
    ExecuteCodeResponse executeCode(@RequestBody ExecuteCodeRequest executeCodeRequest, HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader(AUTH_REQUEST_HEADER);
        if (!AUTH_REQUEST_STR.equals(header)) {
            response.setStatus(403);
            return null;
        }
        if (executeCodeRequest == null) {
            throw new RuntimeException("请求参数为空");
        }
        return javaDockerCodeSandboxTemplate.executeCode(executeCodeRequest);
    }

}
