package com.everelp.keyMaker.config.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CorsFilter implements Filter {

        /*跨域请求配置*/
        @Override
        public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
            HttpServletResponse response = (HttpServletResponse) res;
            HttpServletRequest request = (HttpServletRequest) req;

            String method= request.getMethod();
            String originHeads = request.getHeader("Origin");
//            response.setHeader("Access-Control-Allow-Origin","*");
            // 浏览器疯狂限制跨域，*是不可以的
            response.setHeader("Access-Control-Allow-Origin",originHeads);
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
            response.setHeader("Access-Control-Max-Age", "36000");
            response.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,Authorization,Token");

            if (method.equals("OPTIONS")){
                response.setStatus(200);
                return;
            }

            chain.doFilter(req, response);
        }
        @Override
        public void init(FilterConfig filterConfig) {}
        @Override
        public void destroy() {}


}
