package com.sheldon.devlab.backend.model.dto.tag;

import com.sheldon.devlab.backend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 标签分页查询请求
 *
 * @author <a href="https://github.com/sheldon-3601e">sheldon</a>
 * @from <a href="https://github.com/sheldon-3601e">sheldon</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TagPageQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 标签数量
     */
    private Integer number;

    private static final long serialVersionUID = 1L;
}