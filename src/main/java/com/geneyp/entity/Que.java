package com.geneyp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author GeneYP
 * @version 1.0
 * @date 2022/8/5 06:12
 * @description com.geneyp.entity
 */
@Data
public class Que {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer shopId;
    private Integer gameId;
    private String detail;
    private Integer version;
}
