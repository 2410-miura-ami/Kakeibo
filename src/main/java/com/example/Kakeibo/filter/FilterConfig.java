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
        //login画面,ユーザ登録画面以外の全ての画面にフィルターを設定
        bean.addUrlPatterns("/");
        bean.addUrlPatterns("/newRecord");
        bean.addUrlPatterns("/houseHold/*");
        bean.addUrlPatterns("/showRecord/*");
        bean.addUrlPatterns("/editRecord/*");
        bean.addUrlPatterns("/history");
        bean.setOrder(1);
        return bean;
    }
}
