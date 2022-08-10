package com.xiaopi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author GeneYP
 * @version 1.0
 * @date 2022/7/12 14:56
 * @description com.xiaopi
 */
@Slf4j
@SpringBootApplication
public class MaiQueueApplication {
    public static void main(String[] args) {
        SpringApplication.run(MaiQueueApplication.class, args);
        log.info("小屁已就位! ");
    }
}
