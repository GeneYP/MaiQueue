package com.geneyp.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author GeneYP
 * @version 1.0
 * @date 2022/7/31 06:31
 * @description com.geneyp.vo
 */
@Data
@ApiModel(description = "登录参数")
public class WxUser {
    // 用户唯一标识
    private String uuid;
    // 微信openid
    private String openid;
    // 微信名
    private String nickname;
    // 查分器名
    //private String name;
}
