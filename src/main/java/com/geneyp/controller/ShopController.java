package com.geneyp.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.geneyp.common.R;
import com.geneyp.entity.Que;
import com.geneyp.entity.Shop;
import com.geneyp.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author GeneYP
 * @version 1.0
 * @date 2022/8/4 07:58
 * @description com.geneyp.controller
 */
@RestController
@RequestMapping("/shop")
@Slf4j
public class ShopController {

    @Resource
    private ShopService shopService;

    /**
     * 所有
     *
     * @return allShop
     */
    @GetMapping(value = "/all")
    public R<List<Shop>> getAllShop() {
        try {
            return R.success(shopService.list(), "查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.unsuccess("查询失败：" + e.getMessage());
        }
    }

    /**
     * 分页列表
     *
     * @return shopPage
     */
    @GetMapping(value = "/list")
    public R<IPage<Shop>> getPage(int current, int size) {
        try {
            Page<Shop> page = new Page<>(current, size);
            return R.success(shopService.page(page), "查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.unsuccess("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据关键词模糊查询店铺信息
     *
     * @return shopList
     */
    @GetMapping(value = "/name/{keyword}")
    public R<List<Shop>> getShopByName(@PathVariable String keyword) {
        try {
            return R.success(shopService.getByKeyword(keyword), "查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.unsuccess("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据id查询店铺信息
     *
     * @return shop
     */
    @GetMapping(value = "/id/{id}")
    public R<Shop> getShopById(@PathVariable Integer id) {
        try {
            return R.success(shopService.getById(id), "查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.unsuccess("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据城市查询店铺信息
     *
     * @return shopList
     */
    @GetMapping(value = "/city/{city}")
    public R<List<Shop>> getShopByCity(@PathVariable String city) {
        try {
            return R.success(shopService.getByCity(city), "查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.unsuccess("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据经纬度查询周边店铺
     *
     * @return shopList
     */
    @PostMapping(value = "/around")
    public R<List<Shop>> getShopAround(@RequestBody Shop shop) {
        try {
            BigDecimal lng = shop.getLng();
            BigDecimal lat = shop.getLat();
            return R.success(shopService.getByLngAndLat(lng, lat), "查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.unsuccess("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据定位判断是否在机厅附近
     *
     * @return Que
     */
    @GetMapping(value = "/checkPosition/{lng}/{lat}/{shopId}")
    public R<Que> checkPosition(@PathVariable Integer shopId, @PathVariable BigDecimal lng, @PathVariable BigDecimal lat) {
        try {
            boolean flag = shopService.checkIsArrive(shopId, lng, lat);
            if (flag) {
                return R.success(1, "查询成功，你在机厅");
            } else {
                return R.success(0, "查询成功，你没在机厅");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.unsuccess("查询失败：" + e.getMessage());
        }
    }
}
