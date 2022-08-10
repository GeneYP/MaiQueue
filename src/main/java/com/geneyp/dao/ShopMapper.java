package com.geneyp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geneyp.entity.Shop;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author GeneYP
 * @version 1.0
 * @date 2022/8/4 07:58
 * @description com.geneyp.dao
 */
@Mapper
public interface ShopMapper extends BaseMapper<Shop> {
    List<Shop> getAround(BigDecimal myLng, BigDecimal myLat);
}
