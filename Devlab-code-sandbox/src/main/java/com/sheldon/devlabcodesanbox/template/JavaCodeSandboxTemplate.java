package com.sheldon.devlabcodesanbox.template;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.sheldon.devlabcodesanbox.CodeSandbox;
import com.sheldon.devlabcodesanbox.model.ExecuteCodeRequest;
import com.sheldon.devlabcodesanbox.model.ExecuteCodeResponse;
import com.sheldon.devlabcodesanbox.model.ExecuteMessage;
import com.sheldon.devlabcodesanbox.model.JudgeInfo;
import com.sheldon.devlabcodesanbox.utils.ProcessUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author sheldon
 * @version 1.0
 * @className JavaCodeSndboxTemplate
 * @date 2024/3/7 15:12
 * @description Java 代码沙箱模板方法实现
 */
// TODO 抽象作用
public abstract class JavaCodeSandboxTemplate implements CodeSandbox {

    public static final String GLOBAL_CODE_DIR_NAME = "tmpCode";

    public static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    private static final long TIME_LIMIT = 5000;

    /**
     * 1.隔离保存用户代码的文件
     *
     * @param code
     * @return
     */
    public File saveFile(String code) {
        String userDir = System.getProperty("user.dir");
        String globalCodePathName = userDir + File.separator + GLOBAL_CODE_DIR_NAME;
        // 判断全局代码是否存在，没有则新建
        if (!FileUtil.exist(globalCodePathName)) {
            FileUtil.mkdir(globalCodePathName);
        }
        // 1.把用户的代码隔离存放
        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();
        String userCodePath = userCodeParentPath + File.separator + GLOBAL_JAVA_CLASS_NAME;
        File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);
        return userCodeFile;
    }

    /**
     * 2.编译代码文件
     *
     * @param userCodeFile
     * @return
     */
    public ExecuteMessage compileFile(File userCodeFile) {

        // 2.编译代码，得到class文件
        String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsolutePath());
        try {
            Process compileProcess = Runtime.getRuntime().exec(compileCmd);
            ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(compileProcess, "编译");
            if (executeMessage.getExitValue() != 0) {
                throw new RuntimeException("compile error: " + executeMessage.getErrorMessage());
            }
            return executeMessage;
        } catch (Exception e) {
            throw new RuntimeException("compile error", e);
        }
    }

    /**
     * 3.运行代码文件，获得执行结果
     *
     * @param inputList
     * @param userCodeFile
     * @return
     */
    public List<ExecuteMessage> runFile(List<String> inputList, File userCodeFile) {
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String input : inputList) {
            // -Xmx256m 设置最大堆内存为256m，防止代码无限申请内存
            String runCmd = String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s Main %s", userCodeParentPath, input);
            // 设置安全管理器
            // java -Dfile.encoding=UTF-8 -cp %s;%s -Djava.security.manager=MySecurityManager Main
            try {
                Process runProcess = Runtime.getRuntime().exec(runCmd);
                // 设置超时时间，防止代码无限执行
                new Thread(() -> {
                    try {
                        Thread.sleep(TIME_LIMIT);
                        runProcess.destroy();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
                ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(runProcess, "运行");
                executeMessageList.add(executeMessage);
            } catch (Exception e) {
                throw new RuntimeException("run error", e);
            }
        }
        return executeMessageList;
    }

    /**
     * 4.获取输出结果，封装返回值
     *
     * @param executeMessageList
     * @return
     */
    public ExecuteCodeResponse getOutputResponse(List<ExecuteMessage> executeMessageList) {

        // 4.收集整理输出结果
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setStatus(1);
        List<String> outputList = new ArrayList<>();
        long maxTime = 0;
        for (ExecuteMessage executeMessage : executeMessageList) {
            String errorMessage = executeMessage.getErrorMessage();
            if (StrUtil.isNotBlank(errorMessage)) {
                // 代码执行异常
                executeCodeResponse.setMessage(errorMessage);
                executeCodeResponse.setStatus(3);
                break;
            }
            outputList.add(executeMessage.getMessage());
            Long time = executeMessage.getTime();
            if (time != null) {
                maxTime = Math.max(maxTime, time);
            }
        }
        executeCodeResponse.setOutputList(outputList);
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setTime(maxTime);
//        judgeInfo.setMemory();
        executeCodeResponse.setJudgeInfo(judgeInfo);

        return executeCodeResponse;
    }

    /**
     * 5.删除文件
     *
     * @param userCodeFile
     * @return
     */
    public boolean delFile(File userCodeFile) {

        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
        boolean del = false;
        if (userCodeFile.getParentFile() != null) {
            del = FileUtil.del(userCodeParentPath);
            System.out.println("删除" + (del ? "成功" : "失败"));
        }
        return del;
    }

    /**
     * 6.获取异常返回值
     *
     * @param e
     * @return
     */
    public ExecuteCodeResponse getErrorResponse(Throwable e) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(new ArrayList<>());
        executeCodeResponse.setMessage(e.getMessage());
        // 表示代码沙箱错误
        executeCodeResponse.setStatus(2);
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        return executeCodeResponse;
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {

        List<String> inputList = executeCodeRequest.getInputList();
        String language = executeCodeRequest.getLanguage();
        String code = executeCodeRequest.getCode();

        // 1.把用户的代码隔离存放
        File userCodeFile = saveFile(code);

        // 2.编译代码，得到class文件
        ExecuteMessage compileMessage = compileFile(userCodeFile);

        // 3.运行代码，并输入测试用例
        List<ExecuteMessage> executeMessages = runFile(inputList, userCodeFile);

        // 4.处理返回结果，封装返回值
        ExecuteCodeResponse executeCodeResponse = getOutputResponse(executeMessages);

        // 5. 文件清理
        boolean delFile = delFile(userCodeFile);
        if (!delFile) {
            throw new RuntimeException("del file error");
        }

        return executeCodeResponse;
    }


}
