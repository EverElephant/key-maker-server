package com.everelp.keyMaker.common.vo;

import lombok.Data;

@Data
public class ServerRes<T> {
    private int code;
    private String msg;
    private T data;

    public ServerRes(int i) {
        ResCodeMsg resCodeMsg = ResCodeMsg.getBycode(i);
        this.code = resCodeMsg.code;
        this.msg = resCodeMsg.msg;
    }

    public ServerRes(ResCodeMsg resCodeMsg){
        this.code = resCodeMsg.code;
        this.msg = resCodeMsg.msg;
    }

    public void setCodeMsg(ResCodeMsg resCodeMsg){
        this.code = resCodeMsg.code;
        this.msg = resCodeMsg.msg;
    }

}
