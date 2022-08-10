package com.geneyp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.geneyp.common.R;
import com.geneyp.entity.Music;
import com.geneyp.entity.User;
import com.geneyp.service.UserService;
import com.geneyp.service.WxAuthService;
import com.geneyp.vo.MaiUser;
import com.geneyp.vo.WxUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.Future;

/**
 * @author GeneYP
 * @version 1.0
 * @date 2022/7/13 20:21
 * @description com.xiaopi.controller
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private WxAuthService wxAuthService;

    @GetMapping(value = "/getUser")
    public R<User> getUser(HttpServletRequest request) {
        try {
            // 根据token获取微信openid
            String token = wxAuthService.getToken(request);
            WxUser wxUser = wxAuthService.getWxUser(token);
            // 根据openid获取用户信息（包括查分器）
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("openid", wxUser.getOpenid());
            User user = userService.getOne(wrapper);
            // 封装返回
            MaiUser maiUser = new MaiUser();
            maiUser.setAvatar(user.getAvatar());
            maiUser.setCardImg(user.getCardImg());
            maiUser.setNickname(user.getNickname());
            maiUser.setName(user.getName());
            // 异步回调 请求查分器 只需要返回level就够了
            if (StringUtils.isNotEmpty(user.getName()) && !user.getName().equals("")) {
                // 获取段位分和全部成绩
//                long start = System.currentTimeMillis();
//                log.info("开始查询成绩");
                Future<HashMap<String, Object>> b40Score = userService.getB40Score(user.getName());
//                Future<HashMap<String, Object>> allScore = userService.getAllScore(user.getName());
//                maiUser.setAllScore((String) allScore.get().get("score"));
                maiUser.setAllScore((String) b40Score.get().get("b25"));
//                long end = System.currentTimeMillis();
//                log.info("查询结束，用时：" + (end - start));
                maiUser.setLevel((Integer) b40Score.get().get("level"));
                maiUser.setRating((Integer) b40Score.get().get("rating"));
                maiUser.setB25Score((String) b40Score.get().get("b25"));
                maiUser.setB15Score((String) b40Score.get().get("b15"));
            } else {
                return R.success(maiUser, "查询成功");
            }
            return R.success(maiUser, "查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.unsuccess("查询失败：" + e.getMessage());
        }
    }

    @GetMapping(value = "/setAvatar")
    public R<User> getMaiScore(HttpServletRequest request, String avatar) {
        try {
            // 根据token获取微信openid
            String token = wxAuthService.getToken(request);
            WxUser wxUser = wxAuthService.getWxUser(token);
            // 根据openid获取用户信息（包括查分器）
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("openid", wxUser.getOpenid());
            User user = userService.getOne(wrapper);
            user.setAvatar(avatar);
            userService.saveOrUpdate(user);
            return R.success("修改头像成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.unsuccess("查询失败：" + e.getMessage());
        }
    }

    @PostMapping(value = "/update")
    public R<User> updateUser(@RequestBody User userNew, HttpServletRequest request) {
        try {
            // 根据token获取微信openid
            String token = wxAuthService.getToken(request);
            WxUser wxUser = wxAuthService.getWxUser(token);
            // 根据openid获取用户信息（包括查分器）
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("openid", wxUser.getOpenid());
            User user = userService.getOne(wrapper);
            user.setNickname(userNew.getNickname());
            user.setName(userNew.getName());
            user.setAvatar(userNew.getAvatar());
            userService.saveOrUpdate(user);
            return R.success("修改信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.unsuccess("修改失败：" + e.getMessage());
        }
    }

    @PostMapping(value = "/updateCard")
    public R<User> updateUserCard(@RequestBody User userNew, HttpServletRequest request) {
        try {
            // 根据token获取微信openid
            String token = wxAuthService.getToken(request);
            WxUser wxUser = wxAuthService.getWxUser(token);
            // 根据openid获取用户信息（包括查分器）
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("openid", wxUser.getOpenid());
            User user = userService.getOne(wrapper);
            user.setCardImg(userNew.getCardImg());
            userService.saveOrUpdate(user);
            return R.success("修改卡背成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.unsuccess("修改失败：" + e.getMessage());
        }
    }

    @GetMapping(value = "/logout")
    public R<User> logout(HttpServletRequest request) {
        try {
            // 根据token获取微信openid
            String token = wxAuthService.getToken(request);
            WxUser wxUser = wxAuthService.getWxUser(token);
            // 清除redis
            wxAuthService.clearRedis(wxUser);
            return R.success("注销成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.unsuccess("查询失败：" + e.getMessage());
        }
    }
}
