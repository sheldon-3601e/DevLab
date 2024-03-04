package com.sheldon.devlab.model.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.sheldon.devlab.model.dto.question.JudgeCase;
import com.sheldon.devlab.model.dto.questionSubmit.JudgeConfig;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 题目编辑封装类
 *
 * @TableName question
 */
@Data
public class QuestionEditVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tags;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 判题用例（json 数组）
     */
    private String judgeCase;

    /**
     * 判题配置（json 对象）
     */
    private JudgeConfig judgeConfig;

    private static final long serialVersionUID = 1L;
}