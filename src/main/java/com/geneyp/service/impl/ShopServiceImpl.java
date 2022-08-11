package com.geneyp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geneyp.common.Constant;
import com.geneyp.dao.ShopMapper;
import com.geneyp.entity.Shop;
import com.geneyp.service.ShopService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements ShopService {

    @Resource
    private ShopMapper shopMapper;

    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

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

    @Override
    public boolean checkIsArrive(Integer shopId, BigDecimal lng, BigDecimal lat) {
        QueryWrapper<Shop> wrapper = new QueryWrapper<>();
        wrapper.eq("shop_id", shopId);
        Shop shop = getOne(wrapper);
        double radLat1 = rad(shop.getLat().doubleValue());
        double radLat2 = rad(lat.doubleValue());
        double a = radLat1 - radLat2;
        double b = rad(shop.getLng().doubleValue()) - rad(lng.doubleValue());
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * Constant.EARTH_RADIUS;
        s = Math.round(s * 1000);
        log.info("距离机厅有: " + s);
        if (s > 0.4) {
            log.info("该用户没有到机厅");
            return false;
        } else {
            log.info("该用户到机厅了");
            return true;
        }
    }
}
