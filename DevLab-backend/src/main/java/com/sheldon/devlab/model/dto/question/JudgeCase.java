package com.sheldon.devlab.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName JudgeCase
 * @Author 26483
 * @Date 2024/3/2 15:44
 * @Version 1.0
 * @Description 题目判题用例
 */
@Data
public class JudgeCase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 输入
     */
    private String input;

    /**
     * 输出
     */
    private String output;

}
