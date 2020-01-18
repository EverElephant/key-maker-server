package com.everelp.keyMaker.common.db;

import lombok.Data;

@Data
public class User {
    String userName;
    String password;
    long createTime;
    long updateTime;

    public String toString() {
        return "userName=" + userName
                + "&password=" + password
                + "&createTime=" + createTime
                + "&updateTime=" + updateTime
                ;
    }
}
