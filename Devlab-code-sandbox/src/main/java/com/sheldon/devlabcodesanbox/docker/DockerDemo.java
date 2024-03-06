package com.sheldon.devlabcodesanbox.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.LogContainerResultCallback;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

/**
 * @author sheldon
 * @version 1.0
 * @className DockerDemo
 * @date 2024/3/6 10:02
 * @description docker示例
 */
public class DockerDemo {


    public static void main(String[] args) throws InterruptedException {

        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
        // 拉取镜像
        String image = "nginx:latest";
        PullImageCmd pullImageCmd = dockerClient.pullImageCmd(image);
        PullImageResultCallback pullImageResultCallback = new PullImageResultCallback() {
            @Override
            public void onNext(PullResponseItem item) {
                System.out.println("下载镜像：" + item.getStatus());
                super.onNext(item);
            }
        };
        pullImageCmd
                .exec(pullImageResultCallback)
                .awaitCompletion();
        System.out.println("下载完成");

        // 创建容器
        CreateContainerCmd containerCmd = dockerClient.createContainerCmd(image);
        CreateContainerResponse containerResponse = containerCmd
                .withCmd("echo", "hello docker")
                .exec();
        String containerId = containerResponse.getId();
        // ff5ebd47e95358c9f9f3485aaf93adcd039f81ad2f3ffedf1a5152beab191e2c
        System.out.println(containerId);
//        String containerId = "ff5ebd47e95358c9f9f3485aaf93adcd039f81ad2f3ffedf1a5152beab191e2c";

        // 查看容器列表
        ListContainersCmd listContainersCmd = dockerClient.listContainersCmd();
        List<Container> containers = listContainersCmd.exec();
        for(Container container : containers) {
            System.out.println("container info:" + container);
        }

        // 启动容器
        dockerClient.startContainerCmd(containerId).exec();

        LogContainerResultCallback logContainerResultCallback = new LogContainerResultCallback(){
            @Override
            public void onNext(Frame item) {
                System.out.println("My Log:" + new String(item.getPayload()));
                super.onNext(item);
            }
        };

        // 查看容器日志
        dockerClient.logContainerCmd(containerId)
                .withStdErr(true)
                .withStdOut(true)
                .exec(logContainerResultCallback)
                .awaitCompletion();

        //  删除容器
        dockerClient.removeContainerCmd(containerId)
                .withForce(true)
                .exec();

        // 删除镜像
        dockerClient.removeImageCmd(image).exec();
    }
}
