package com.geneyp.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author GeneYP
 * @version 1.0
 * @date 2022/8/5 06:58
 * @description com.geneyp.vo
 */
@Data
@ApiModel(description = "mai队列")
public class Maier {
    // 队列id
    private Integer queId;
    // 机厅id
    private Integer shopId;
    // 机台id
    private Integer gameId;
    // 玩家id
    private String uuid;
    // 玩家位置
    private Integer position;
    // 玩家昵称
    private String nickname;
    // 卡背
    private String cardImg;
    // 队列版本
//    private Integer version;
}
