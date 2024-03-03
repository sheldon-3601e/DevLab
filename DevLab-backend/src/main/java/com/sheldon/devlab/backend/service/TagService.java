package com.sheldon.devlab.backend.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sheldon.devlab.backend.model.entity.Tag;
import com.sheldon.devlab.backend.model.vo.TagVO;

import java.util.List;

/**
* @author 26483
* @description 针对表【tag(标签)】的数据库操作Service
* @createDate 2024-02-11 00:26:13
*/
public interface TagService extends IService<Tag> {

    /**
     * 获取脱敏的用户信息
     *
     * @param tag
     * @return
     */
    TagVO getTagVO(Tag tag);

    /**
     * 获取脱敏的用户信息
     *
     * @param tagList
     * @return
     */
    List<TagVO> getTagVO(List<Tag> tagList);

    /**
     * 查询查询次数靠前的标签列表
     *
     * @param number
     * @return
     */
    List<TagVO> listTopTagVO(Integer number);
}
