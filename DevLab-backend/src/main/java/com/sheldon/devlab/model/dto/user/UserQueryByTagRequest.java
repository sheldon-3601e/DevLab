package com.sheldon.devlab.model.dto.user;

import com.sheldon.devlab.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 用户查询请求
 *
 * @author <a href="https://github.com/sheldon-3601e">sheldon</a>
 * @from <a href="https://github.com/sheldon-3601e">sheldon</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryByTagRequest extends PageRequest implements Serializable {

    /**
     * 用户输入搜索内容
     */
    private String searchKey;

    /**
     * 标签列表
     */
    private List<String> tagNameList;

    private static final long serialVersionUID = 1L;
}