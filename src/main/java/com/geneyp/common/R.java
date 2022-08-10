package com.geneyp.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author GeneYP
 * @version 1.0
 * @date 2022/7/13 20:14
 * @description 统一返回类
 */
@Data
public class R<T>
{
    @ApiModelProperty(value = "响应状态")
    private int code;

    private T data;

    @ApiModelProperty(value = "描述")
    private String desc;

    @ApiModelProperty(value = "业务是否成功")
    private Boolean success;


    /**
     * 初始化一个新创建的 R 对象
     *
     * @param code 状态码
     * @param data 数据对象
     */
    public R(int code, Boolean success, T data, String desc)
    {
        this.code=code;
        this.success=success;
        this.data=data;
        this.desc=desc;
    }

    /**
     * 返回成功消息
     *
     * @param data 数据对象
     * @return 成功消息
     */
    public static R success(Object data)
    {
        return new R(200,true,data,null);
    }
    /**
     * 返回成功消息
     *
     * @param data 数据对象
     * @return 成功消息
     */
    public static R success(Object data,String desc)
    {
        return new R(200,true,data,desc);
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static R unsuccess(String desc)
    {
        return new R(500,false,null,desc);
    }


    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static R unsuccess(String data, String desc)
    {
        return new R(200,false,data,desc);
    }


    /**
     * 无权限
     *
     * @return 成功消息
     */
    public static R notAuth()
    {
        return new R(401,false,null, "没有权限");
    }

    /**
     * 返回错误消息
     *
     * @return 警告消息
     */
    public static R error(String msg)
    {
        return new R(500,false,null, msg);
    }

    /**
     * 返回错误消息
     *
     * @return 警告消息
     */
    public static R notfound()
    {
        return new R(404,false,null, "接口未找到");
    }

    /**
     * 返回错误消息
     *
     * @return 警告消息
     */
    public static R notReadable()
    {
        return new R(400,false,null, "参数解析失败");
    }

}
