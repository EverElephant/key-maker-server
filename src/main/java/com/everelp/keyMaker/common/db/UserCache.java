package com.everelp.keyMaker.common.db;

import lombok.Data;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 是个用户与session的集合，sessionId作为用户主键
 * 用户未注册，cache里通过sessionId查不到用户信息，转到登录页面(filter)
 * 用户注册后，当前sessionId作为key存入用户信息(register)
 * 用户关闭浏览器或者session换了之后，用户需重新登录(filter)
 * 登录成功用户信息变更key,换为新的sessionId(login)
 */
public class UserCache {
    public static ConcurrentHashMap<String, User> cache = new ConcurrentHashMap<>();
}
