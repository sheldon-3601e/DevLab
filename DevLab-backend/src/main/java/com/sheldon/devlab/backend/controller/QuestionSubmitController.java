package com.sheldon.devlab.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sheldon.devlab.backend.common.BaseResponse;
import com.sheldon.devlab.backend.common.ErrorCode;
import com.sheldon.devlab.backend.common.ResultUtils;
import com.sheldon.devlab.backend.exception.BusinessException;
import com.sheldon.devlab.backend.model.dto.questionSubmit.QuestionSubmitAddRequest;
import com.sheldon.devlab.backend.model.dto.questionSubmit.QuestionSubmitQueryRequest;
import com.sheldon.devlab.backend.model.entity.QuestionSubmit;
import com.sheldon.devlab.backend.model.entity.User;
import com.sheldon.devlab.backend.model.vo.QuestionSubmitVO;
import com.sheldon.devlab.backend.service.QuestionSubmitService;
import com.sheldon.devlab.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户提交问题接口
 *
 * @author <a href="https://github.com/sheldon-3601e">sheldon</a>
 */
@RestController
@RequestMapping("/question_submit")
@Slf4j
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest
     * @param request
     * @return resultNum 本次点赞变化数
     */
    @PostMapping("/")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
                                               HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        Long result = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 分页题目提交获取列表
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest, HttpServletRequest request) {
        long current = questionSubmitQueryRequest.getCurrent();
        long size = questionSubmitQueryRequest.getPageSize();
        // 从数据库中查询原始的题目提交分页信息
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size),
                questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
        final User loginUser = userService.getLoginUser(request);
        // 返回脱敏信息
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage, loginUser));
    }

}
