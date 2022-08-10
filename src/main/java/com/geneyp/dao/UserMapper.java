package com.geneyp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geneyp.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author GeneYP
 * @version 1.0
 * @date 2022/7/13 20:31
 * @description com.xiaopi.mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
