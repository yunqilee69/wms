package com.yunqi.backend.exception.message;

import com.yunqi.backend.common.base.BaseError;

/**
 * 库存盘点单错误类
 * @author liyunqi
 */
public enum InventoryCheckError implements BaseError {
    STATUS_IS_APPLY_DEL(60001, "盘点单已应用，不允许删除!"),
    STATUS_IS_APPLY_UPDATE(60002, "盘点单已应用，不允许更新!"),
    ;

    int code;

    String msg;

    InventoryCheckError(int code, String msg) {
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
