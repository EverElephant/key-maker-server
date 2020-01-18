package com.everelp.keyMaker.common.util;

import com.everelp.keyMaker.common.db.User;
import com.everelp.keyMaker.common.db.UserCache;

import javax.servlet.http.HttpServletRequest;

public class UserUtil {
    public static User getCurrentUser(HttpServletRequest request){
        String sessionId = request.getSession().getId();
        return UserCache.cache.get(sessionId);
    }
}
