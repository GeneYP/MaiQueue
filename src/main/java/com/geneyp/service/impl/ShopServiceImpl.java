package com.geneyp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geneyp.dao.ShopMapper;
import com.geneyp.entity.Shop;
import com.geneyp.service.ShopService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author GeneYP
 * @version 1.0
 * @date 2022/8/4 08:04
 * @description com.geneyp.service.impl
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements ShopService {

    @Resource
    private ShopMapper shopMapper;

    @Override
    public List<Shop> getByKeyword(String keyword) {
        QueryWrapper<Shop> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", keyword)
                .or()
                .like("address", keyword);
        return list(queryWrapper);
    }

    @Override
    public List<Shop> getByCity(String city) {
        QueryWrapper<Shop> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("city", city);
        return list(queryWrapper);
    }

    @Override
    public List<Shop> getByLngAndLat(BigDecimal lng, BigDecimal lat) {
        return shopMapper.getAround(lng, lat);
    }
}
