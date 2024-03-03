package com.sheldon.devlab.model.dto.questionSubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * 提交题目请求
 *
 * @author 26483
 * @date 2024/03/02
 */
@Data
public class QuestionSubmitAddRequest implements Serializable {

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 题目 id
     */
    private Long questionId;

    private static final long serialVersionUID = 1L;

}