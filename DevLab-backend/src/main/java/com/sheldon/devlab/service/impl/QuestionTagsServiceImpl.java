package com.sheldon.devlab.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sheldon.devlab.mapper.QuestionTagsMapper;
import com.sheldon.devlab.model.entity.QuestionTags;
import com.sheldon.devlab.service.QuestionTagsService;
import org.springframework.stereotype.Service;

/**
* @author 26483
* @description 针对表【question_tags(标签)】的数据库操作Service实现
* @createDate 2024-03-04 00:53:26
*/
@Service
public class QuestionTagsServiceImpl extends ServiceImpl<QuestionTagsMapper, QuestionTags>
    implements QuestionTagsService {

}




