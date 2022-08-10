package com.geneyp.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geneyp.dao.MaiLogMapper;
import com.geneyp.entity.MaiLog;
import com.geneyp.service.MaiLogService;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author GeneYP
 * @version 1.0
 * @date 2022/8/5 17:20
 * @description com.geneyp.service.impl
 */
@Service
@Slf4j
public class MaiLogServiceImpl extends ServiceImpl<MaiLogMapper, MaiLog> implements MaiLogService {

    @Resource
    MaiLogMapper maiLogMapper;

    @Override
    public List<MaiLog> getByIds(Integer shopId, Integer gameId) {
        QueryWrapper<MaiLog> wrapper = new QueryWrapper<>();
        wrapper.eq("shop_id", shopId).eq("game_id", gameId);
        wrapper.orderByDesc("id");
        //查询最高10条数据
        wrapper.last("limit 0,10");
        return list(wrapper);
    }
}
