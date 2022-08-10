package com.geneyp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.geneyp.entity.User;

import java.util.HashMap;
import java.util.concurrent.Future;

/**
 * @author GeneYP
 * @version 1.0
 * @date 2022/7/13 20:31
 * @description com.xiaopi.service
 */
public interface UserService extends IService<User> {

    Future<HashMap<String, Object>> getAllScore(String name);

    Future<HashMap<String, Object>> getB40Score(String name);
}
