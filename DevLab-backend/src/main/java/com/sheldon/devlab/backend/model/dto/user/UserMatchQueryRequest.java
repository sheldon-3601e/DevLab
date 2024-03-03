package com.sheldon.devlab.backend.model.dto.user;

import com.sheldon.devlab.backend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 匹配用户查询请求
 *
 * @author <a href="https://github.com/sheldon-3601e">sheldon</a>
 * @from <a href="https://github.com/sheldon-3601e">sheldon</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserMatchQueryRequest extends PageRequest implements Serializable {

    /**
     * 匹配用户数量
     */
    private Integer matchNum;

    private static final long serialVersionUID = 1L;
}