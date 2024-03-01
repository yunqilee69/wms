package com.yunqi.backend.common.result;

import lombok.Data;

/**
 * 分页查询结果
 * @author liyunqi
 */
@Data
public class PageResult {

    /** 当前记录起始索引 */
    private Integer pageNum;

    /** 每页显示记录数 */
    private Integer pageSize;

    /**
     * 总记录数
     */
    private Integer total;

    /**
     * 实际结果
     */
    private Object rows;


}
