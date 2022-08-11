package com.geneyp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.geneyp.entity.Shop;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author GeneYP
 * @version 1.0
 * @date 2022/8/4 08:05
 * @description com.geneyp.service
 */
public interface ShopService extends IService<Shop> {

    List<Shop> getByKeyword(String keyword);

    List<Shop> getByCity(String city);

    List<Shop> getByLngAndLat(BigDecimal shopLng, BigDecimal shopLat);

    boolean checkIsArrive(Integer shopId, BigDecimal lng, BigDecimal lat);
}
