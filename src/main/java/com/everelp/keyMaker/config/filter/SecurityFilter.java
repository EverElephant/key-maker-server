package com.everelp.keyMaker.config.filter;

import com.everelp.keyMaker.common.db.User;
import com.everelp.keyMaker.common.db.UserCache;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

public class SecurityFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String uri = httpRequest.getRequestURI();

        HttpSession session = httpRequest.getSession();
        String sessionId = session.getId();
        // session如果没有登陆信息
        if (!UserCache.cache.containsKey(sessionId)) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(401);
            return;
        }

        User user = UserCache.cache.get(sessionId);
        user.setUpdateTime(new Date().getTime());
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
