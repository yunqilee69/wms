package com.yunqi.backend.exception.message;

import com.yunqi.backend.common.base.BaseError;

/**
 * @author liyunqi
 */
public enum OrderError implements BaseError {
    ORDER_ID_IS_EMPTY(80001, "订单ID为空"),
    FIND_RECORD_ERROR(80002, "查找库存记录错误"),
    ORDER_DETAIL_ID_EMPTY(80003, "订单细节id为空"),
    AMOUNT_IS_EMPTY(80004, "金额为空"),
    ORDER_STATUS_IS_NOT_1(80005, "订单状态不是已新建，操作失败"),
    ORDER_STATUS_IS_NOT_2(80006, "订单状态不是已收货，操作失败"),
    SALE_ORDER_STATUS_IS_NOT_2(80007, "订单状态不是已送货，操作失败");
    private int code;
    private String msg;

    OrderError(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getErrorMsg() {
        return this.msg;
    }
}
