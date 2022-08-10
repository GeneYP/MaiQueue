package com.geneyp;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

/**
 * @author GeneYP
 * @version 1.0
 * @date 2022/7/12 14:56
 * @description com.xiaopi
 */
@Slf4j
@SpringBootApplication
@EnableAsync
@EnableScheduling
@MapperScan(value = "com.geneyp.dao")
public class MaiQueueApplication {
    @Autowired
    private RestTemplateBuilder builder;
    // 使用RestTemplateBuilder来实例化RestTemplate对象，spring默认已经注入了RestTemplateBuilder实例
    @Bean
    public RestTemplate restTemplate() {
        return builder.build();
    }
    public static void main(String[] args) {
        SpringApplication.run(MaiQueueApplication.class, args);
        log.info("小屁已就位! ");
    }
}
