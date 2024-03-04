package com.sheldon.devlab.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sheldon.devlab.Judge.JudgeService;
import com.sheldon.devlab.Judge.strategy.JudgeStrategy;
import com.sheldon.devlab.common.ErrorCode;
import com.sheldon.devlab.constant.CommonConstant;
import com.sheldon.devlab.exception.BusinessException;
import com.sheldon.devlab.mapper.QuestionSubmitMapper;
import com.sheldon.devlab.model.dto.questionSubmit.QuestionSubmitAddRequest;
import com.sheldon.devlab.model.dto.questionSubmit.QuestionSubmitQueryRequest;
import com.sheldon.devlab.model.entity.Question;
import com.sheldon.devlab.model.entity.QuestionSubmit;
import com.sheldon.devlab.model.entity.User;
import com.sheldon.devlab.model.enums.QuestionSubmitLanguageEnum;
import com.sheldon.devlab.model.enums.QuestionSubmitStatusEnum;
import com.sheldon.devlab.model.vo.QuestionSubmitVO;
import com.sheldon.devlab.service.QuestionService;
import com.sheldon.devlab.service.QuestionSubmitService;
import com.sheldon.devlab.service.UserService;
import com.sheldon.devlab.utils.SqlUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author sheldon
 * @description 针对表【question_submit(题目提交)】的数据库操作Service实现
 * @createDate 2024-03-02 15:25:26
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService {

    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    @Resource
    @Lazy
    private JudgeService judgeService;

    @Override
    public Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        // 判断题目是否存在
        Long questionId = questionSubmitAddRequest.getQuestionId();
        String code = questionSubmitAddRequest.getCode();
        String language = questionSubmitAddRequest.getLanguage();

        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 校验编程语言是否合法
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }
        // 每个用户串行提交
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setLanguage(language);
        questionSubmit.setCode(code);
        questionSubmit.setJudgeInfo("{}");
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setUserId(loginUser.getId());

        // TODO 限流，防止用户提交过快
        boolean save = this.save(questionSubmit);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据插入失败");
        }
        // TODO 调用代码沙箱
        Long questionSubmitId = questionSubmit.getId();
        CompletableFuture.runAsync(() -> judgeService.doJudge(questionSubmitId));
        return questionSubmitId;
    }

    /**
     * 获取查询包装类
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        // 脱敏：仅本人和管理员能看见自己（提交 userId 和登录用户 id 不同）提交的代码
        long userId = loginUser.getId();
        // 处理脱敏
        if (userId != questionSubmit.getUserId() && !userService.isAdmin(loginUser)) {
            questionSubmitVO.setCode(null);
        }
        return questionSubmitVO;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollectionUtils.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream()
                .map(questionSubmit -> getQuestionSubmitVO(questionSubmit, loginUser))
                .collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }
}





