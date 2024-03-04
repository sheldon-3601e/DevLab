package com.sheldon.devlab.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sheldon.devlab.mapper.TagMapper;
import com.sheldon.devlab.model.entity.Tag;
import com.sheldon.devlab.model.vo.TagVO;
import com.sheldon.devlab.service.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author sheldon
* @description 针对表【tag(标签)】的数据库操作Service实现
* @createDate 2024-02-11 00:26:13
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService {

    @Resource
    private TagMapper tagMapper;

    @Override
    public TagVO getTagVO(Tag tag) {
        if (tag == null) {
            return null;
        }
        TagVO tagVO = new TagVO();
        BeanUtils.copyProperties(tag, tagVO);
        return tagVO;
    }

    @Override
    public List<TagVO> getTagVO(List<Tag> tagList) {
        if (CollUtil.isEmpty(tagList)) {
            return new ArrayList<>();
        }
        return tagList.stream().map(this::getTagVO).collect(Collectors.toList());
    }

    @Override
    public List<TagVO> listTopTagVO(Integer number) {
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("count");
        queryWrapper.last("limit " + number);
        List<Tag> tagList = tagMapper.selectList(queryWrapper);
        return getTagVO(tagList);
    }
}




