package com.everelp.keyMaker.config;

import com.everelp.keyMaker.config.filter.CorsFilter;
import com.everelp.keyMaker.config.filter.SecurityFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class WebConfig {
    @Bean
    public FilterRegistrationBean corsFilterRegister() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        //注入过滤器
        registration.setFilter(new CorsFilter());
        //拦截规则
        registration.addUrlPatterns("/*");
        //过滤器名称
        registration.setName("corsFilter");
        //过滤器顺序
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public FilterRegistrationBean securityFilterRegister() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        //注入过滤器
        registration.setFilter(new SecurityFilter());
        //拦截规则
        registration.addUrlPatterns("/key/*");
        //过滤器名称
        registration.setName("securityFilter");
        //过滤器顺序
        registration.setOrder(2);
        return registration;
    }
}
