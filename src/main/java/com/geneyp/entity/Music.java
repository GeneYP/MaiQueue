package com.geneyp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

/**
 * @author GeneYP
 * @version 1.0
 * @date 2022/7/23 01:53
 * @description com.geneyp.entity
 */
@Data
@TableName("music")
public class Music {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String title;
    private String type;
    private String ds;
    private String level;
    private String charts;
    private String basicInfo;
    private String nickname;
    private Integer bpm;
    private String genre;
    private String maifrom;
    private String basicDs;
    private String advancedDs;
    private String expertDs;
    private String masterDs;
    private String remasterDs;
    private Integer available;
}
