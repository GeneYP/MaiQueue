package com.geneyp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import java.time.Duration;


/**
 * @author GeneYP
 * @version 1.0
 * @date 2022/8/13 16:02
 * @description com.geneyp.config
 */
@Slf4j
@Configuration
public class RestTemplateConfig {

//    @Autowired
//    private RestTemplateBuilder builder;

//    @Bean
//    @ConfigurationProperties(prefix = "rest-template.connection")
//    public HttpComponentsClientHttpRequestFactory httpRequestFactory() {
//        return new HttpComponentsClientHttpRequestFactory();
//    }
//
//    @Bean
//    public RestTemplate customRestTemplate(){
//        return new RestTemplate(httpRequestFactory());
//    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder)
    {
        return restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(5))
                .build();
    }

    // 使用RestTemplateBuilder来实例化RestTemplate对象，spring默认已经注入了RestTemplateBuilder实例
//    @Bean
//    public RestTemplate restTemplate() {
//        return builder.build();
//    }
}
