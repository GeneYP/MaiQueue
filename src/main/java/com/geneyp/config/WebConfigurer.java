package com.gym.config;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Bean
    public JwtInterceptor jwtInterceptor(){ return new JwtInterceptor(); }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new GlobalInterceptor()).addPathPatterns("/**");
        List<String> excludeLoginList = Lists.newArrayList();
        excludeLoginList.add("/");
        excludeLoginList.add("/favicon.ico");
        excludeLoginList.add("/swagger-resources");
        excludeLoginList.add("/v3/api-docs");
        excludeLoginList.add("/manage/auth/login");
        excludeLoginList.add("/manage/auth/register");
//        excludeLoginList.add("/fc/**");
        excludeLoginList.add("/merchant/gym/search/**");
        excludeLoginList.add("/fc/auth/register");
        excludeLoginList.add("/fc/auth/login");
        excludeLoginList.add("/fc/food/**");
        excludeLoginList.add("/fc/plan/**");
        excludeLoginList.add("/fc/active/**");
        excludeLoginList.add("/common/**");
        registry.addInterceptor(jwtInterceptor()).addPathPatterns("/**").excludePathPatterns(excludeLoginList);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}
