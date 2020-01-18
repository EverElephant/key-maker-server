package com.everelp.keyMaker.common.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResCodeMsg {
    SUCCESS(1000,"成功"),
    FAIL(9999,"失败"),

    FUNCTION_CANT(7000,"功能不允许"),
    MAKER_UNAVAL(7001,"暂无可用生成器，请五分钟后再试"),

    INVALID_OPER(8000,"操作不合适"),
    HAVE_REGISTERED(8001,"您已注册"),
    NAME_REGISTERED(8002,"用户名已被注册"),



    WRONG(9000,"错误"),
    NO_USER(9001,"用户不存在"),
    PWD_WRONG(9002,"密码错误"),

    ;
    int code;
    String msg;

    public static ResCodeMsg getBycode(int code){
        for (ResCodeMsg codeMsg : ResCodeMsg.values()) {
            if(code == codeMsg.code){
                return codeMsg;
            }
        }
        return FAIL;
    }
}
