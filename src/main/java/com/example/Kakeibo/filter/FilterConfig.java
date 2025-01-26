package com.example.Kakeibo.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    //ログインフィルター
    @Bean
    public FilterRegistrationBean<LoginFilter> loginFilter() {
        FilterRegistrationBean<LoginFilter> bean = new FilterRegistrationBean<>();

        bean.setFilter(new LoginFilter());
        //login画面以外の全ての画面にフィルターを設定
        bean.addUrlPatterns("/");
        bean.setOrder(1);
        return bean;
    }
}
