package com.sheldon.devlab.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 标签返回对象
 * @TableName tag
 */
@Data
public class TagVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 标签内容
     */
    private String tagName;

    /**
     * 父标签 id
     */
    private Long parentId;

    /**
     * 0 - 不是， 1 - 是 父标签
     */
    private Integer isParent;

    private static final long serialVersionUID = 1L;
}