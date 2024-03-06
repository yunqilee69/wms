package com.yunqi.backend.exception.message;

import com.yunqi.backend.common.base.BaseError;

/**
 * @author liyunqi
 */
public enum RecordError implements BaseError {
    RECORD_ID_IS_EMPTY(50001, "库存记录ID为空"),
    ALARM_THRESHOLD_LT_0(50002, "库存报警阈值必须大于等于0");
    private int code;
    private String msg;

    RecordError(int code, String msg) {
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
