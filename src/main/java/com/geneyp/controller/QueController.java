package com.geneyp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.geneyp.common.R;
import com.geneyp.entity.Que;
import com.geneyp.entity.User;
import com.geneyp.service.QueService;
import com.geneyp.service.UserService;
import com.geneyp.service.WxAuthService;
import com.geneyp.vo.WxUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author GeneYP
 * @version 1.0
 * @date 2022/8/5 17:14
 * @description com.geneyp.controller
 */
@RestController
@RequestMapping("/que")
@Slf4j
public class QueController {

    @Resource
    QueService queService;
    @Resource
    private UserService userService;
    @Resource
    private WxAuthService wxAuthService;

    /**
     * 根据id查询队列信息
     *
     * @return Que
     */
    @GetMapping(value = "/get/{shopId}/{gameId}")
    public R<Que> getQueByIds(@PathVariable Integer shopId, @PathVariable String gameId) {
        try {
            Que que = queService.getByIds(shopId, gameId);
            if (que != null) {
                que.setId(null);
            } else {
                return R.success(null, "查询成功，没人在排队");
            }
            return R.success(que, "查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.unsuccess("查询失败：" + e.getMessage());
        }
    }

    /**
     * 加入队列尾部
     *
     * @return Que
     */
    @GetMapping(value = "/add/{shopId}/{gameId}/{version}")
    public R<Que> addQue(HttpServletRequest request,
                         @PathVariable Integer shopId,
                         @PathVariable Integer gameId,
                         @PathVariable Integer version) {
        try {
            // 根据token获取微信openid
            String token = wxAuthService.getToken(request);
            WxUser wxUser = wxAuthService.getWxUser(token);
            // 根据openid获取用户信息（包括查分器）
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("openid", wxUser.getOpenid());
            User user = userService.getOne(wrapper);
            Que que = queService.addEnd(shopId, gameId, user.getUuid(), version);
            if (que == null) {
                return R.unsuccess("数据已过期，请刷新页面！");
            } else {
                que.setId(null);
                return R.success(que, "查询成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.unsuccess("查询失败：" + e.getMessage());
        }
    }

    /**
     * 退出队列
     *
     * @return Que
     */
    @GetMapping(value = "/quit/{shopId}/{gameId}/{position}/{version}")
    public R<Que> quitQue(@PathVariable Integer shopId,
                          @PathVariable Integer gameId,
                          @PathVariable Integer position,
                          @PathVariable Integer version) {
        try {
            Que que = queService.quitByPosition(shopId, gameId, position, version);
            if (que == null) {
                return R.unsuccess("数据已过期，请刷新页面！");
            } else {
                que.setId(null);
                return R.success(que, "查询成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.unsuccess("查询失败：" + e.getMessage());
        }
    }

    /**
     * 队首回到队列尾
     *
     * @return Que
     */
    @GetMapping(value = "/push/{shopId}/{gameId}/{version}")
    public R<Que> pushQue(@PathVariable Integer shopId,
                          @PathVariable Integer gameId,
                          @PathVariable Integer version) {
        try {
            Que que = queService.pushByIds(shopId, gameId, version);
            if (que == null) {
                return R.unsuccess("数据已过期，请刷新页面！");
            } else {
                que.setId(null);
                return R.success(que, "查询成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.unsuccess("查询失败：" + e.getMessage());
        }
    }

    /**
     * 退出队列
     *
     * @return Que
     */
    @GetMapping(value = "/move/{shopId}/{gameId}/{cardFrom}/{cartTo}/{version}")
    public R<Que> moveQue(@PathVariable Integer shopId,
                          @PathVariable Integer gameId,
                          @PathVariable Integer cardFrom,
                          @PathVariable Integer cartTo,
                          @PathVariable Integer version) {
        try {
            Que que = queService.moveByPosition(shopId, gameId, cardFrom, cartTo, version);
            if (que == null) {
                return R.unsuccess("数据已过期，请刷新页面！");
            } else {
                que.setId(null);
                return R.success(que, "查询成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.unsuccess("查询失败：" + e.getMessage());
        }
    }
}
