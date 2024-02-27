package com.yunqi.backend.common.base;

import lombok.Data;

/**
 * 基础分页请求对象
 *
 */
@Data
public class BasePageQuery {

    private int pageNum = 1;

    private int pageSize = 10;
}
