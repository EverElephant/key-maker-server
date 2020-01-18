package com.everelp.keyMaker.mudules.controller;

import com.everelp.keyMaker.common.db.User;
import com.everelp.keyMaker.common.db.UserCache;
import com.everelp.keyMaker.common.vo.ResCodeMsg;
import com.everelp.keyMaker.common.vo.ServerRes;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;


@RestController
@RequestMapping("sys")
public class SysController {

    @PostMapping("login")
    public ServerRes login(User user, HttpServletRequest request) {
        ServerRes serverRes = new ServerRes(ResCodeMsg.SUCCESS);
        String oldSessionId = null;
        if(StringUtils.isEmpty(user.getUserName())|| StringUtils.isEmpty(user.getPassword())){
            serverRes.setCodeMsg(ResCodeMsg.EMPTY_PARAM);
            return serverRes;
        }
        HttpSession session = request.getSession();
        if (UserCache.cache.get(session.getId()) != null) {
            // 明明登录了还要再登录,直接返回成功
            return serverRes;
        }
        // 查询用户信息
        for (String key : UserCache.cache.keySet()) {
            User o = UserCache.cache.get(key);
            String un = o.getUserName();
            if (un.equals(user.getUserName())) {
                if (o.getPassword().equals(user.getPassword())) {
                    user = o;
                    oldSessionId = key;
                } else {
                    serverRes.setCodeMsg(ResCodeMsg.PWD_WRONG);
                    return serverRes;
                }
            }
        }
        // 用户不存在
        if (user.getCreateTime() == 0L) {
            serverRes.setCodeMsg(ResCodeMsg.NO_USER);
            return serverRes;
        }
        // 有用户信息，清除原来的信息存入新的信息
        UserCache.cache.put(session.getId(), user);
        UserCache.cache.remove(oldSessionId);

        return serverRes;
    }

    @PostMapping("register")
    public ServerRes register(User user, HttpServletRequest request) {
        ServerRes serverRes = new ServerRes(ResCodeMsg.SUCCESS);

        if(StringUtils.isEmpty(user.getUserName())|| StringUtils.isEmpty(user.getPassword())){
            serverRes.setCodeMsg(ResCodeMsg.EMPTY_PARAM);
            return serverRes;
        }
        HttpSession session = request.getSession();
        if (UserCache.cache.get(session.getId()) != null) {
            return serverRes;
        }
        // 查询用户名
        for (String key : UserCache.cache.keySet()) {
            User o = UserCache.cache.get(key);
            String un = o.getUserName();
            if (un.equals(user.getUserName())) {
                serverRes.setCodeMsg(ResCodeMsg.NAME_REGISTERED);
                return serverRes;
            }
        }
        user.setCreateTime(new Date().getTime());
        user.setUpdateTime(new Date().getTime());

        // 有用户信息，清除原来的信息存入新的信息
        UserCache.cache.put(session.getId(), user);

        return serverRes;
    }
}
