package com.geneyp.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.geneyp.service.MaiLogService;
import com.geneyp.service.QueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author GeneYP
 * @version 1.0
 * @date 2022/8/10 17:02
 * @description com.geneyp.util
 */
@Component
@Slf4j
@Lazy(value = false)
@EnableScheduling
public class MaiScheduler {

    @Resource
    MaiLogService maiLogService;

    @Resource
    QueService queService;

    /**
     * 每天凌晨5点10分执行一次
     */
    @Scheduled(cron = "10 5 * * * ? ")
    public void clearMaiBoard(){
        log.info("开始清空日志");
        maiLogService.remove(new QueryWrapper<>());
        log.info("开始清空排卡板");
        queService.remove(new QueryWrapper<>());
    }
}
