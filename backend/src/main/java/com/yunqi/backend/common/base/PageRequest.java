package com.yunqi.backend.common.base;

import lombok.Data;

/**
 * 分页查询请求，存放分页查询的基本信息
 * @author liyunqi
 */
@Data
public class PageRequest {

    /** 当前记录起始索引 */
    private Integer pageNum;

    /** 每页显示记录数 */
    private Integer pageSize;

    /** 排序列 */
    private String orderByColumn;

    /** 排序的方向desc或者asc */
    private String isAsc = "asc";

    /**
     * 分页参数合理化
     * 当true时，请求的页数大于实际数据的页数，会返回最后一页的数据而不是空
     * */
    private Boolean reasonable = true;

}
