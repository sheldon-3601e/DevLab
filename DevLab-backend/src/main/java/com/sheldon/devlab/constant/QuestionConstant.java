package com.sheldon.devlab.constant;

/**
 * 题目相关常量
 *
 * @author <a href="https://github.com/sheldon-3601e">sheldon</a>
 * @from <a href="https://github.com/sheldon-3601e">sheldon</a>
 */
public interface QuestionConstant {

    /**
     * 限制用户提交题目的时间间隔
     * 1000L * 60 * 5 = 5 分钟
     */
    String QUESTION_SUBMIT_LIMIT_NAME = "devlab:question:submit:limit";

    /**
     * 用户同时判题题目的数量限制
     */
    Integer QUESTION_SUBMIT_LIMIT = 10;

    /**
     * 降序
     */
    String SORT_ORDER_DESC = " descend";

}
