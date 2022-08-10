package com.geneyp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author GeneYP
 * @version 1.0
 * @date 2022/8/4 07:59
 * @description com.geneyp.entity
 */
@Data
@TableName("shop")
public class Shop {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private String province;
    private String city;
    private String address;
    private Integer num;
    private BigDecimal lng;
    private BigDecimal lat;
    private Integer isAuto;
}
