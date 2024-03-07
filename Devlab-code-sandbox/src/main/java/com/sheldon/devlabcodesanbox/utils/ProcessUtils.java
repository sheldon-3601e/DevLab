package com.sheldon.devlabcodesanbox.utils;

import cn.hutool.core.util.StrUtil;
import com.sheldon.devlabcodesanbox.model.ExecuteMessage;
import org.springframework.util.StopWatch;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sheldon
 * @version 1.0
 * @className ProcessUtils
 * @date 2024/3/5 20:39
 * @description 进程工具类
 */

public class ProcessUtils {

    /**
     * 执行进程并获取执行信息
     *
     * @param runProcess
     * @param opName
     * @return
     */
    public static ExecuteMessage runProcessAndGetMessage(Process runProcess, String opName) {
        ExecuteMessage executeMessage = new ExecuteMessage();

        try {
             // 记录开始时间
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            int exitValue = runProcess.waitFor();
            executeMessage.setExitValue(exitValue);
            // 正常退出
            if (exitValue == 0) {
                System.out.println(opName + "成功");
                // 分批获取控制台的正常输出
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
                // 逐行读取
                List<String> compileOutputList = new ArrayList<>();
                String compileOutputLine;
                while ((compileOutputLine = bufferedReader.readLine()) != null) {
                    compileOutputList.add(compileOutputLine);
                }
                executeMessage.setMessage(StrUtil.join("\n", compileOutputList));

            } else {
                // 异常退出
                System.out.println(opName + "失败，错误码：" + exitValue);
                // 分批获取控制台的正常输出
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
                // 逐行读取
                List<String> compileOutputList = new ArrayList<>();
                String compileOutputLine;
                while ((compileOutputLine = bufferedReader.readLine()) != null) {
                    compileOutputList.add(compileOutputLine);
                }
                executeMessage.setMessage(StrUtil.join("\n", compileOutputList));

                // 分批获取控制台的正常输出
                BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
                // 逐行读取
                List<String> errorCompileOutputList = new ArrayList<>();
                String errorCompileOutputLine;
                while ((errorCompileOutputLine = errorBufferedReader.readLine()) != null) {
                    errorCompileOutputList.add(errorCompileOutputLine);
                }
                executeMessage.setErrorMessage(StrUtil.join("\n", errorCompileOutputList));
            }
            stopWatch.stop();
            // 封装执行时间
            executeMessage.setTime(stopWatch.getTotalTimeMillis());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return executeMessage;
    }

    /**
     * 执行交互进程并获取执行信息
     *
     * @param runProcess
     * @param args
     * @return
     */
    public static ExecuteMessage runInteractProcessAndGetMessage(Process runProcess, String args) {
        ExecuteMessage executeMessage = new ExecuteMessage();

        try {
            // 向控制台输入程序
            OutputStream outputStream = runProcess.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            String[] s = args.split(" ");
            String join = StrUtil.join("\n", s) + "\n";
            outputStreamWriter.write(join);
            // 相当于按了回车，执行输入的发送
            outputStreamWriter.flush();

            // 分批获取进程的正常输出
            InputStream inputStream = runProcess.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder compileOutputStringBuilder = new StringBuilder();
            // 逐行读取
            String compileOutputLine;
            while ((compileOutputLine = bufferedReader.readLine()) != null) {
                compileOutputStringBuilder.append(compileOutputLine);
            }
            executeMessage.setMessage(compileOutputStringBuilder.toString());
            // 记得资源的释放，否则会卡死
            outputStreamWriter.close();
            outputStream.close();
            inputStream.close();
            runProcess.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return executeMessage;
    }

}
