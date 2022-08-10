package com.geneyp.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author GeneYP
 * @version 1.0
 * @date 2022/8/3 09:51
 * @description com.geneyp.vo mai用户
 */
@Data
@ApiModel(description = "mai用户参数")
public class MaiUser {
    // 微信openid
    private String openid;
    // 微信名
    private String nickname;
    // 用户头像
    private String avatar;
    // 查分器名
    private String name;
    // 底分
    private Integer rating;
    // 段位
    private Integer level;
    // 卡背
    private String cardImg;
    // 所有成绩
    private String allScore;
    // b25
    private String b25Score;
    // b15
    private String b15Score;
}
