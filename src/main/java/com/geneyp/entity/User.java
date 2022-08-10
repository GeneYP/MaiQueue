package com.geneyp.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author GeneYP
 * @version 1.0
 * @date 2022/7/13 20:21
 * @description 用户实体类
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 3505783006495761499L;

    private Long id;

    private String uuid;

    private String name;

    private String password;

    private String avatar;

    private String nickname;

    private String openid;

    private Integer gender;

    private String cardImg;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public void setNull() {
        id = null;
        openid = null;
    }
}
