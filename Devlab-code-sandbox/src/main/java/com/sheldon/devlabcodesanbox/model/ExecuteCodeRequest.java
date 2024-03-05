package com.sheldon.devlabcodesanbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName ExecuteCodeRequest
 * @Author sheldon
 * @Date 2024/3/4 15:55
 * @Version 1.0
 * @Description 代码沙箱请求类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeRequest {

    private List<String> inputList;

    private String language;

    private String code;

}
