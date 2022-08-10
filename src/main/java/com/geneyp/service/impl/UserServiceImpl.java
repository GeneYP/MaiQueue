package com.geneyp.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geneyp.common.Constant;
import com.geneyp.common.R;
import com.geneyp.entity.User;
import com.geneyp.dao.UserMapper;
import com.geneyp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author GeneYP
 * @version 1.0
 * @date 2022/7/13 20:30
 * @description com.xiaopi.service.impl
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    @Async
    public Future<HashMap<String, Object>> getAllScore(String name) {
        long start = System.currentTimeMillis();
        log.info("开始查询" + name + "的所有成绩");
        String apiURL = Constant.ALL_SCORE_URL + name;
        HttpHeaders headers = new HttpHeaders();
        headers.add(Constant.DEV_TOKEN_HEADER, Constant.DEV_TOKEN_VALUE);
        // header的格式设置为json
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> requestParam = new HashMap<>();
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestParam, headers);
        HashMap<String, Object> allScore = new HashMap<>();
        try {
            ResponseEntity<String> entity = restTemplate.exchange(apiURL, HttpMethod.GET, request, String.class);
            String body = entity.getBody();
            // String转为Json对象
            JSONObject jsonObject = JSON.parseObject(body);
            Object records = jsonObject.get("records");
            // Json转为String对象
            String score = records.toString();
            allScore.put("score", score);
            allScore.put("state", 1);
        } catch (HttpStatusCodeException e) {
            log.error(e.getMessage(), e);
            allScore.put("score", "");
            allScore.put("state", 0);
        }
        long end = System.currentTimeMillis();
        log.info("查询所有成绩用时：" + (end-start));
        return new AsyncResult<>(allScore);
    }

    @Override
    @Async
    public Future<HashMap<String, Object>> getB40Score(String name) {
        long start = System.currentTimeMillis();
        log.info("开始查询" + name + "的Best40成绩");
        String apiURL = Constant.B40_SCORE_URL;
        HttpHeaders headers = new HttpHeaders();
        // header的格式设置为json
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 请求体
        Map<String, Object> requestParam = new HashMap<>();
        requestParam.put("username", name);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestParam, headers);
        HashMap<String, Object> b40Score = new HashMap<>();
        try {
            ResponseEntity<String> entity = restTemplate.exchange(apiURL, HttpMethod.POST, request, String.class);
            String body = entity.getBody();
            // String转为Json对象
            JSONObject jsonObject = JSON.parseObject(body);
            Integer level = (Integer) jsonObject.get("additional_rating");
            Integer rating = (Integer) jsonObject.get("rating");
            JSONObject records = (JSONObject) jsonObject.get("charts");
            // Json转为String对象
            String sd = String.valueOf(records.getJSONArray("sd"));
            String dx = String.valueOf(records.getJSONArray("dx"));
            b40Score.put("level", level);
            b40Score.put("rating", rating + level);
            b40Score.put("b25", sd);
            b40Score.put("b15", dx);
        } catch (HttpStatusCodeException e) {
            log.error(e.getMessage(), e);
            b40Score.put("level", 0);
            b40Score.put("rating", 0);
            b40Score.put("b25", "");
            b40Score.put("b15", "");
        }
        long end = System.currentTimeMillis();
        log.info("查询b40用时：" + (end-start));
        return new AsyncResult<>(b40Score);
    }
}
