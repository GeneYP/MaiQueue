package com.xiaopi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaopi.entity.User;
import com.xiaopi.mapper.UserMapper;
import com.xiaopi.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author GeneYP
 * @version 1.0
 * @date 2022/7/13 20:30
 * @description com.xiaopi.service.impl
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
