package com.sheldon.devlab.controller;

import com.sheldon.devlab.exception.BusinessException;
import com.sheldon.devlab.common.BaseResponse;
import com.sheldon.devlab.common.ErrorCode;
import com.sheldon.devlab.common.ResultUtils;
import com.sheldon.devlab.exception.ThrowUtils;
import com.sheldon.devlab.model.dto.tag.TagQueryRequest;
import com.sheldon.devlab.model.entity.Tag;
import com.sheldon.devlab.model.vo.TagVO;
import com.sheldon.devlab.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户接口
 *
 * @author <a href="https://github.com/sheldon-3601e">sheldon</a>
 * @from <a href="https://github.com/sheldon-3601e">sheldon</a>
 */
@RestController
@RequestMapping("/tag")
@Slf4j
public class TagController {

    @Resource
    private TagService tagService;

    /**
     * 获取所有标签列表
     *
     * @param request
     * @return
     */
    @PostMapping("/list/vo")
    public BaseResponse<List<TagVO>> listTagVO(
            HttpServletRequest request) {
        List<Tag> tagList = tagService.list();
        List<TagVO> tagVOList = tagService.getTagVO(tagList);
        return ResultUtils.success(tagVOList);
    }

    /**
     * 获取查询次数最多的标签
     *
     * @param tagQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/top/vo")
    public BaseResponse<List<TagVO>> listTagVOByTop(@RequestBody TagQueryRequest tagQueryRequest,
                                                    HttpServletRequest request) {
        if (tagQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Integer number = tagQueryRequest.getNumber();
        // 限制爬虫
        ThrowUtils.throwIf(number > 20, ErrorCode.PARAMS_ERROR);
        List<TagVO> tagVO = tagService.listTopTagVO(number);
        return ResultUtils.success(tagVO);
    }
}
