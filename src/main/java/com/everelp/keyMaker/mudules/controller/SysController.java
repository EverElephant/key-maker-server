package com.everelp.keyMaker.mudules.controller;

import com.everelp.keyMaker.common.db.User;
import com.everelp.keyMaker.common.db.UserCache;
import com.everelp.keyMaker.common.vo.ResCodeMsg;
import com.everelp.keyMaker.common.vo.ServerRes;
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
        System.out.println(user.toString());
        ServerRes commonRes = new ServerRes(ResCodeMsg.SUCCESS);
        String oldSessionId = null;

        HttpSession session = request.getSession();
        if (UserCache.cache.get(session.getId()) != null) {
            // 明明登录了还要再登录,直接返回成功
            return commonRes;
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
                    commonRes.setCodeMsg(ResCodeMsg.PWD_WRONG);
                    return commonRes;
                }
            }
        }
        // 用户不存在
        if (user.getCreateTime() == 0L) {
            commonRes.setCodeMsg(ResCodeMsg.NO_USER);
            return commonRes;
        }
        // 有用户信息，清除原来的信息存入新的信息
        UserCache.cache.put(session.getId(), user);
        UserCache.cache.remove(oldSessionId);

        return commonRes;
    }

    @PostMapping("register")
    public ServerRes register(User user, HttpServletRequest request) {
        System.out.println(user.toString());
        ServerRes commonRes = new ServerRes(ResCodeMsg.SUCCESS);

        HttpSession session = request.getSession();
        if (UserCache.cache.get(session.getId()) != null) {
            commonRes.setCodeMsg(ResCodeMsg.HAVE_REGISTERED);
            return commonRes;
        }
        // 查询用户名
        for (String key : UserCache.cache.keySet()) {
            User o = UserCache.cache.get(key);
            String un = o.getUserName();
            if (un.equals(user.getUserName())) {
                commonRes.setCodeMsg(ResCodeMsg.NAME_REGISTERED);
                return commonRes;
            }
        }
        user.setCreateTime(new Date().getTime());
        user.setUpdateTime(new Date().getTime());

        // 有用户信息，清除原来的信息存入新的信息
        UserCache.cache.put(session.getId(), user);

        return commonRes;
    }
}
