package com.sheldon.devlab.Judge;

import com.sheldon.devlab.model.entity.QuestionSubmit;

/**
 * @ClassName JudgeService
 * @Author sheldon
 * @Date 2024/3/4 17:00
 * @Version 1.0
 * @Description 判题服务接口
 */
public interface JudgeService {

    QuestionSubmit doJudge(Long questionSubmitId);

}
