package com.sheldon.devlabcodesanbox;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.sheldon.devlabcodesanbox.model.ExecuteCodeRequest;
import com.sheldon.devlabcodesanbox.model.ExecuteCodeResponse;
import com.sheldon.devlabcodesanbox.model.ExecuteMessage;
import com.sheldon.devlabcodesanbox.model.JudgeInfo;
import com.sheldon.devlabcodesanbox.utils.ProcessUtils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author sheldon
 * @version 1.0
 * @className JavaNativeCodeSandboxImpl
 * @date 2024/3/5 19:47
 * @description Java docker 代码沙箱实现
 */
public class JavaDockerCodeSandboxImpl implements CodeSandbox {

    public static final String GLOBAL_CODE_DIR_NAME = "tmpCode";

    public static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    private static final long TIME_LIMIT = 5000;

    // 判断是否初始化，需要拉取镜像
    private static final Boolean IS_INIT = false;

    public static void main(String[] args) {
        JavaDockerCodeSandboxImpl javaNativeCodeSandbox = new JavaDockerCodeSandboxImpl();
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setInputList(Arrays.asList("1 2", "1 3"));
        executeCodeRequest.setLanguage("java");
        String code = ResourceUtil.readStr("testCode/simpleComputeArgs/Main.java", StandardCharsets.UTF_8);
        executeCodeRequest.setCode(code);

        ExecuteCodeResponse executeCodeResponse = javaNativeCodeSandbox.executeCode(executeCodeRequest);
        System.out.println(executeCodeResponse);
    }

    /**
     * 1. 拉取镜像
     * 2. 把用户的代码保存为文件
     * 3. 编译代码，得到 class 文件
     * 4. 创建容器，将 class 文件目录 挂载到 容器内部
     * 5. 启动容器
     * 6. 在容器内部用 输入值 执行 class 文件，获取输出值
     * 7. 收集整理输出结果
     * 8. 文件清理，释放空间
     * 9. 错误处理，提升程序健壮性
     *
     * @param executeCodeRequest
     * @return
     */
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {

        List<String> inputList = executeCodeRequest.getInputList();
        String language = executeCodeRequest.getLanguage();
        String code = executeCodeRequest.getCode();


        String userDir = System.getProperty("user.dir");
        String globalCodePathName = userDir + File.separator + GLOBAL_CODE_DIR_NAME;
        // 判断 全局代码目录 是否存在，没有则新建目录
        if (!FileUtil.exist(globalCodePathName)) {
            FileUtil.mkdir(globalCodePathName);
        }
        // 1.创建新的文件夹，将用户代码写入文件，隔离存放
        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();
        String userCodePath = userCodeParentPath + File.separator + GLOBAL_JAVA_CLASS_NAME;
        File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);

        // 2.得到代码文件的绝对路径，
        String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsolutePath());
        try {
            // 编译代码，得到class文件
            Process compileProcess = Runtime.getRuntime().exec(compileCmd);
            ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(compileProcess, "编译");
            System.out.println(executeMessage);
        } catch (Exception e) {
            return getErrorResponse(e);
        }

        // 3.判断是否需要拉取镜像
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
        String image = "openjdk:8-alpine";
        if (IS_INIT) {
            try {
                // 拉取镜像
                PullImageCmd pullImageCmd = dockerClient.pullImageCmd(image);
                PullImageResultCallback pullImageResultCallback = new PullImageResultCallback() {
                    @Override
                    public void onNext(PullResponseItem item) {
                        System.out.println("下载镜像：" + item.getStatus());
                        super.onNext(item);
                    }
                };
                // 通过回调函数，获得异步操作的返回值
                pullImageCmd
                        .exec(pullImageResultCallback)
                        .awaitCompletion();

                System.out.println("拉取镜像成功");
            } catch (InterruptedException e) {
                System.out.println("拉取镜像失败");
                throw new RuntimeException(e);
            }
        }

        // 4.创建容器
        CreateContainerCmd containerCmd = dockerClient.createContainerCmd(image);
        // 创建配置文件
        HostConfig hostConfig = new HostConfig();
        // 将代码目录挂载在容器的 "/app" 目录下
        hostConfig.setBinds(new Bind(userCodeParentPath, new Volume("/app")));
        // 限制内存大小
        hostConfig.withMemory(100 * 1000 * 1000L);
        hostConfig.withMemorySwap(0L);
        // 限制 CPU 核数
        hostConfig.withCpuCount(1L);
        CreateContainerResponse containerResponse = containerCmd
                .withHostConfig(hostConfig)
                // 禁止网络请求
                .withNetworkDisabled(true)
                // 禁止向 root 根目录下写文件
                .withReadonlyRootfs(true)
                // 开启TTL模式，便于我们后续在容器中，执行 java 命令
                // 表示在创建容器时开启了 TTY（终端）模式。TTY 模式允许用户可以在容器中执行交互式命令，
                // 即在容器中启动一个终端会话，类似于在本地启动一个终端会话。
                .withTty(true)
                .withAttachStdin(true)
                .withAttachStderr(true)
                .withAttachStdout(true)
                .exec();
        // 得到 容器 Id ，便于后续操作
        String containerId = containerResponse.getId();

        // 5.启动容器
        dockerClient.startContainerCmd(containerId).exec();

        // 在容器中执行命令。运行 java .class文件，并获取结果
        // docker 指令：
        // Usage:  docker exec [OPTIONS] CONTAINER COMMAND [ARG...]
        // Execute a command in a running container
        // docker exec festive_feynman java -cp /app Main 1 3
        // 收集程序执行结果
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String inputArgs : inputList) {
            // 分割输入值
            String[] inputArgsList = inputArgs.split(" ");
            // 拼接 java 命令
            String[] cmdArray = ArrayUtil.append(new String[]{"java", "-cp", "/app", "Main"}, inputArgsList);

            // 创建命令 -> docker exec
            ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerId)
                    // 添加 java 命令
                    .withCmd(cmdArray)
                    .withAttachStdin(true)
                    .withAttachStderr(true)
                    .withAttachStdout(true)
                    .exec();
            System.out.println("创建执行命令：" + execCreateCmdResponse);
            String execId = execCreateCmdResponse.getId();

            ExecuteMessage executeMessage = new ExecuteMessage();
            Long time = 0L;
            final Long[] memory = {0L};
            final String[] message = {null};
            final String[] errorMessage = {null};

            // 监控内存的回调方法
            ResultCallback<Statistics> startResultCallback = new ResultCallback<Statistics>() {

                @Override
                public void onNext(Statistics statistics) {
                    Long memoryUsage = statistics.getMemoryStats().getUsage();
                    System.out.println("内存占用：" + memoryUsage);
                    memory[0] = Math.max(memoryUsage, memory[0]);
                }

                @Override
                public void onStart(Closeable closeable) {

                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onComplete() {

                }

                @Override
                public void close() throws IOException {

                }
            };
            // 开启容器的内存监控
            StatsCmd statsCmd = dockerClient.statsCmd(containerId);
            statsCmd.exec(startResultCallback);

            // 标识是否超时
            final Boolean[] timeout = {true};
            // 执行命令的回调方法
            ExecStartResultCallback execStartResultCallback = new ExecStartResultCallback() {
                @Override
                public void onComplete() {
                    // 如果程序正常执行，则会执行方法
                    // 则证明没有超时
                    timeout[0] = false;
                    super.onComplete();
                }

                @Override
                public void onNext(Frame frame) {
                    // 获取命令的结果
                    StreamType streamType = frame.getStreamType();
                    if (StreamType.STDERR.equals(streamType)) {
                        errorMessage[0] = new String(frame.getPayload());
                        System.out.println("输出错误结果：" + errorMessage[0]);
                    } else {
                        message[0] = new String(frame.getPayload());
                        System.out.println("输出结果：" + message[0]);
                    }
                    super.onNext(frame);
                }
            };
            // 开启监控 程序执行时间
            StopWatch stopWatch = new StopWatch();
            try {
                stopWatch.start();
                // 6.在容器中 执行 java程序
                dockerClient.execStartCmd(execId)
                        .exec(execStartResultCallback)
                        // 设置超时时间
                        .awaitCompletion(TIME_LIMIT, TimeUnit.MILLISECONDS);
                stopWatch.stop();
                // 关闭内容监控，否则会一直执行
                statsCmd.close();

                System.out.println("删除容器");
            } catch (InterruptedException e) {
                System.out.println("容器启动错误");
                throw new RuntimeException(e);
            }


            // 7.封装执行信息
            time = stopWatch.getLastTaskTimeMillis();
            executeMessage.setMessage(message[0]);
            executeMessage.setErrorMessage(errorMessage[0]);
            executeMessage.setTime(time);
            executeMessage.setMemory(memory[0]);
            executeMessageList.add(executeMessage);
        }

        // 8.收集整理输出结果
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setStatus(1);
        List<String> outputList = new ArrayList<>();
        long maxTime = 0;
        long maxMemory = 0L;
        for (ExecuteMessage executeMessage : executeMessageList) {
            String errorMessage = executeMessage.getErrorMessage();
            if (StrUtil.isNotBlank(errorMessage)) {
                // 代码执行异常
                executeCodeResponse.setMessage(errorMessage);
                executeCodeResponse.setStatus(3);
                break;
            }
            outputList.add(executeMessage.getMessage());
            // 取最大时间
            Long time = executeMessage.getTime();
            if (time != null) {
                maxTime = Math.max(maxTime, time);
            }
            // 取最大内存
            Long memory = executeMessage.getMemory();
            if (memory != null) {
                maxMemory = Math.max(maxMemory, memory);
            }

        }
        executeCodeResponse.setOutputList(outputList);
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setTime(maxTime);
        judgeInfo.setMemory(maxMemory);

        executeCodeResponse.setJudgeInfo(judgeInfo);

        return executeCodeResponse;
    }

    /**
     * 获取异常返回值
     *
     * @param e
     * @return
     */
    private ExecuteCodeResponse getErrorResponse(Throwable e) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(new ArrayList<>());
        executeCodeResponse.setMessage(e.getMessage());
        // 表示代码沙箱错误
        executeCodeResponse.setStatus(2);
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        return executeCodeResponse;
    }
}

