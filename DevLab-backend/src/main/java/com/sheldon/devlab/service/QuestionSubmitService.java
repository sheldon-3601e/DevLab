package com.sheldon.devlab.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sheldon.devlab.model.dto.questionSubmit.QuestionSubmitAddRequest;
import com.sheldon.devlab.model.dto.questionSubmit.QuestionSubmitQueryRequest;
import com.sheldon.devlab.model.entity.QuestionSubmit;
import com.sheldon.devlab.model.entity.User;
import com.sheldon.devlab.model.vo.QuestionSubmitVO;

/**
 * @author sheldon
 * @description 针对表【question_submit(题目提交)】的数据库操作Service
 * @createDate 2024-03-02 15:25:26
 */
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 获取查询条件
     * @param questionSubmitSubmitSubmitSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitSubmitSubmitSubmitQueryRequest);

    /**
     * 获取题目提交封装
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取题目提交封装
     * @param questionSubmitPage
     * @param loginUser
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);

    boolean increaseQuestionSubmitCount(Long questionId);

    boolean increaseQuestionPassCount(Long questionId);
}
