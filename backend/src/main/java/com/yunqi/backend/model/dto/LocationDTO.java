package com.yunqi.backend.model.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yunqi.backend.common.base.BaseDTO;
import com.yunqi.backend.common.base.BaseEntity;
import lombok.Data;

/**
 * @author liyunqi
 */
@Data
public class LocationDTO extends BaseDTO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 货位名称
     */
    private String name;

    /**
     * 状态
     */
    private String status;

    /**
     * 容量
     */
    private String capacity;

    /**
     * 备注
     */
    private String remark;

}
