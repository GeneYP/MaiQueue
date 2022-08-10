package com.geneyp.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geneyp.dao.QueMapper;
import com.geneyp.dao.UserMapper;
import com.geneyp.entity.Que;
import com.geneyp.entity.User;
import com.geneyp.service.QueService;
import com.geneyp.vo.Maier;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * @author GeneYP
 * @version 1.0
 * @date 2022/8/5 17:20
 * @description com.geneyp.service.impl
 */
@Service
@Slf4j
public class QueServiceImpl extends ServiceImpl<QueMapper, Que> implements QueService {

    @Resource
    QueMapper queMapper;
    @Resource
    UserMapper userMapper;

    @Override
    public Que getByIds(Integer shopId, String gameId) {
        QueryWrapper<Que> wrapper = new QueryWrapper<>();
        wrapper.eq("shop_id", shopId).eq("game_id", gameId);
        return getOne(wrapper);
    }

    @Override
    public Que addEnd(Integer shopId, Integer gameId, String uuid, Integer version) {
        QueryWrapper<Que> wrapper = new QueryWrapper<>();
        wrapper.eq("shop_id", shopId).eq("game_id", gameId);
        Que que = queMapper.selectOne(wrapper);
        if (que == null) {
            // 不存在记录的时候新增一条记录到数据库
            Que newQue = new Que();
            newQue.setShopId(shopId);
            newQue.setGameId(gameId);
            newQue.setVersion(1);
            // detail字段内容
            LinkedList<Maier> maiers = new LinkedList<>();
            Maier maier = new Maier();
            Que result = setMaierByUser(shopId, gameId, uuid, 0, maiers, maier, newQue);
            queMapper.insert(result);
            return result;
        } else {
            // 存在记录的时候更新数据库
            if (version.equals(que.getVersion())) {
                // 如果版本正确，更新数据库
                que.setVersion(que.getVersion() + 1);
                String detail = que.getDetail();
                LinkedList<Maier> maiers = new LinkedList<>();
                // 字符串转Json数组
                JsonArray jsonArray = new JsonParser().parse(detail).getAsJsonArray();
                // 遍历Json数组
                for (int i = 0; i < jsonArray.size(); i++) {
                    Maier maier = new Maier();
                    // 获取Json数组内的对象，存入LinkedList
                    JSONObject jsonObject = JSON.parseObject(String.valueOf(jsonArray.get(i)));
                    maier.setUuid((String) jsonObject.get("uuid"));
                    maier.setGameId((Integer) jsonObject.get("gameId"));
                    maier.setShopId((Integer) jsonObject.get("shopId"));
                    maier.setNickname((String) jsonObject.get("nickname"));
                    maier.setCardImg((String) jsonObject.get("cardImg"));
                    maier.setPosition(i);
//                    log.info("maier: " + maier);
                    maiers.addLast(maier);
                }
                Maier maier = new Maier();
                Que result = setMaierByUser(shopId, gameId, uuid, jsonArray.size(), maiers, maier, que);
                queMapper.updateById(result);
                return que;
            } else {
                log.info("版本错误：version = " + que.getVersion());
                return null;
            }
        }
    }

    @Override
    public Que quitByPosition(Integer shopId, Integer gameId, Integer position, Integer version) {
        QueryWrapper<Que> wrapper = new QueryWrapper<>();
        wrapper.eq("shop_id", shopId).eq("game_id", gameId);
        Que que = queMapper.selectOne(wrapper);
        if (que == null || position == null) {
            return null;
        } else {
            // 存在记录的时候更新数据库
            if (version.equals(que.getVersion())) {
                // 如果版本正确，更新数据库
                que.setVersion(que.getVersion() + 1);
                String detail = que.getDetail();
                LinkedList<Maier> maiers = new LinkedList<>();
                // 字符串转Json数组
                JsonArray jsonArray = new JsonParser().parse(detail).getAsJsonArray();
                // 遍历Json数组
                for (int i = 0; i < jsonArray.size(); i++) {
                    Maier maier = new Maier();
                    // 获取Json数组内的对象，存入LinkedList
                    JSONObject jsonObject = JSON.parseObject(String.valueOf(jsonArray.get(i)));
                    maier.setUuid((String) jsonObject.get("uuid"));
                    maier.setGameId((Integer) jsonObject.get("gameId"));
                    maier.setShopId((Integer) jsonObject.get("shopId"));
                    maier.setNickname((String) jsonObject.get("nickname"));
                    maier.setCardImg((String) jsonObject.get("cardImg"));
                    maier.setPosition(i);
                    maiers.addLast(maier);
                }
                maiers.remove(position.intValue());
                // detail字段
                String[] objJson = new String[maiers.toArray().length];
                for (int i = 0; i < maiers.toArray().length; i++) {
                    // 单个obj转json字符串
                    objJson[i] = JSONObject.toJSONString(maiers.toArray()[i]);
                }
                // 多个objJson转数组转JsonString存数据库
                String objJsonArray = Arrays.toString(objJson);
                que.setDetail(objJsonArray);
                queMapper.updateById(que);
                return que;
            } else {
                log.info("版本错误：version = " + que.getVersion());
                return null;
            }
        }
    }

    @Override
    public Que pushByIds(Integer shopId, Integer gameId, Integer version) {
        QueryWrapper<Que> wrapper = new QueryWrapper<>();
        wrapper.eq("shop_id", shopId).eq("game_id", gameId);
        Que que = queMapper.selectOne(wrapper);
        if (que == null) {
            return null;
        } else {
            // 存在记录的时候更新数据库
            if (version.equals(que.getVersion())) {
                // 如果版本正确，更新数据库
                que.setVersion(que.getVersion() + 1);
                String detail = que.getDetail();
                LinkedList<Maier> maiers = new LinkedList<>();
                // 字符串转Json数组
                JsonArray jsonArray = new JsonParser().parse(detail).getAsJsonArray();
                // 遍历Json数组
                for (int i = 0; i < jsonArray.size(); i++) {
                    Maier maier = new Maier();
                    // 获取Json数组内的对象，存入LinkedList
                    JSONObject jsonObject = JSON.parseObject(String.valueOf(jsonArray.get(i)));
                    maier.setUuid((String) jsonObject.get("uuid"));
                    maier.setGameId((Integer) jsonObject.get("gameId"));
                    maier.setShopId((Integer) jsonObject.get("shopId"));
                    maier.setNickname((String) jsonObject.get("nickname"));
                    maier.setCardImg((String) jsonObject.get("cardImg"));
                    maier.setPosition(i);
                    maiers.addLast(maier);
                }
                maiers.addLast(maiers.removeFirst());
                // detail字段
                String[] objJson = new String[maiers.toArray().length];
                for (int i = 0; i < maiers.toArray().length; i++) {
                    // 单个obj转json字符串
                    objJson[i] = JSONObject.toJSONString(maiers.toArray()[i]);
                }
                // 多个objJson转数组转JsonString存数据库
                String objJsonArray = Arrays.toString(objJson);
                que.setDetail(objJsonArray);
                queMapper.updateById(que);
                return que;
            } else {
                log.info("版本错误：version = " + que.getVersion());
                return null;
            }
        }
    }

    @Override
    public Que moveByPosition(Integer shopId, Integer gameId, Integer cardFrom, Integer cartTo, Integer version) {
        QueryWrapper<Que> wrapper = new QueryWrapper<>();
        wrapper.eq("shop_id", shopId).eq("game_id", gameId);
        Que que = queMapper.selectOne(wrapper);
        if (que == null || cardFrom == null || cartTo == null) {
            return null;
        } else {
            // 存在记录的时候更新数据库
            if (version.equals(que.getVersion())) {
                // 如果版本正确，更新数据库
                que.setVersion(que.getVersion() + 1);
                String detail = que.getDetail();
                LinkedList<Maier> maiers = new LinkedList<>();
                // 字符串转Json数组
                JsonArray jsonArray = new JsonParser().parse(detail).getAsJsonArray();
                // 遍历Json数组
                for (int i = 0; i < jsonArray.size(); i++) {
                    Maier maier = new Maier();
                    // 获取Json数组内的对象，存入LinkedList
                    JSONObject jsonObject = JSON.parseObject(String.valueOf(jsonArray.get(i)));
                    maier.setUuid((String) jsonObject.get("uuid"));
                    maier.setGameId((Integer) jsonObject.get("gameId"));
                    maier.setShopId((Integer) jsonObject.get("shopId"));
                    maier.setNickname((String) jsonObject.get("nickname"));
                    maier.setCardImg((String) jsonObject.get("cardImg"));
                    maier.setPosition(i);
                    maiers.addLast(maier);
                }
                Maier maierFrom = maiers.get(cardFrom);
                maiers.remove(maierFrom);
                maiers.add(cartTo, maierFrom);
                // detail字段
                String[] objJson = new String[maiers.toArray().length];
                for (int i = 0; i < maiers.toArray().length; i++) {
                    // 单个obj转json字符串
                    objJson[i] = JSONObject.toJSONString(maiers.toArray()[i]);
                }
                // 多个objJson转数组转JsonString存数据库
                String objJsonArray = Arrays.toString(objJson);
                que.setDetail(objJsonArray);
                queMapper.updateById(que);
                return que;
            } else {
                log.info("版本错误：version = " + que.getVersion());
                return null;
            }
        }
    }

    private Que setMaierByUser(Integer shopId, Integer gameId, String uuid, Integer position, LinkedList<Maier> maiers, Maier maier, Que que) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("uuid", uuid);
        User user = userMapper.selectOne(wrapper);
        maier.setNickname(user.getNickname());
        maier.setCardImg(user.getCardImg());
        maier.setUuid(uuid);
        maier.setPosition(position);
        maier.setShopId(shopId);
        maier.setGameId(gameId);
        maiers.addLast(maier);
        // detail字段
        String[] objJson = new String[maiers.toArray().length];
        for (int i = 0; i < maiers.toArray().length; i++) {
            // 单个obj转json字符串
            objJson[i] = JSONObject.toJSONString(maiers.toArray()[i]);
        }
        // 多个objJson转数组转JsonString存数据库
        String objJsonArray = Arrays.toString(objJson);
        que.setDetail(objJsonArray);
        return que;
    }
}
