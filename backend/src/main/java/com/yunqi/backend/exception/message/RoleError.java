package com.yunqi.backend.exception.message;

import com.yunqi.backend.common.base.BaseError;

/**
 * @author liyunqi
 */
public enum RoleError implements BaseError {
    ID_IS_EMPTY(20001, "角色id不能为空"),
    NAME_IS_EMPTY(20002, "角色名称不能为空"),
    STATUS_IS_EMPTY(20003, "角色状态不能为空"),
    ROLE_KEY_IS_EMPTY(20004, "角色标识不能为空"),
    ORDER_NUM_GT_ZERO(20005, "角色排序号必须大于0"),
    NAME_ALERTER_EXIST(20006, "角色名称已存在"),
    ROLE_KEY_ALERTER_EXIST(20007, "角色标识已存在"),
    ;

    int code;
    String msg;

    RoleError(int code, String msg) {
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
