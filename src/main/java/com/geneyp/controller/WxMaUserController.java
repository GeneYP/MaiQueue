package com.geneyp.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.binarywang.wx.miniapp.util.WxMaConfigHolder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.geneyp.common.R;
import com.geneyp.entity.User;
import com.geneyp.service.UserService;
import com.geneyp.service.WxAuthService;
import com.geneyp.util.IdCreater;
import com.geneyp.vo.WxUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * 微信小程序用户接口
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/wx/user/{appid}")
public class WxMaUserController {
    private final WxMaService wxMaService;

    @Resource
    private WxAuthService wxAuthService;

    @Resource
    private UserService userService;

    /**
     * 登陆接口
     */
    @GetMapping("/login")
    public R login(@PathVariable String appid, String code, String token) {
        WxUser wxUser = null;
        if (StringUtils.isNotEmpty(token)) {
            wxUser = wxAuthService.getWxUser(token);
        }
        // token为空的时候代表需要获取openid wxUser对象为空的时候说明redis过期
        if (StringUtils.isEmpty(token) || (wxUser == null)) {
            if (StringUtils.isBlank(code)) {
                return R.error("res.code为空");
            }
            if (!wxMaService.switchover(appid)) {
                throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
            }
            try {
                WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
                log.info(session.getSessionKey());
                log.info(session.getOpenid());
                wxUser = new WxUser();
                wxUser.setOpenid(session.getOpenid());
                QueryWrapper<User> wrapper = new QueryWrapper<>();
                wrapper.eq("openid", session.getOpenid());
                User user = userService.getOne(wrapper);
                // 用户不为空，未登录
                if (user != null) {
                    // 若用户Nickname不为空，说明手动更新过
                    if (StringUtils.isNotEmpty(user.getNickname())) {
                        wxUser.setNickname(user.getNickname());
                        wxUser.setUuid(user.getUuid());
                        String newToken = wxAuthService.createToken(wxUser);
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("token", newToken);
                        user.setNull();
                        map.put("user", user);
                        return R.success(map, "登录成功");
                    } else {
                        // nickname为空，但是不是第一次登录（存在openid）
                        wxUser.setNickname("椰叶GUEST");
                        String newToken = wxAuthService.createToken(wxUser);
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("token", newToken);
                        map.put("user", null);
                        return R.success(map, "未授权");
                    }
                } else {
                    // 用户为空 说明首次尝试登录
                    String uuid = IdCreater.create15();
                    wxUser.setUuid(uuid);
                    wxUser.setNickname("椰叶GUEST");
                    String newToken = wxAuthService.createToken(wxUser);
                    User user1 = new User();
                    user1.setOpenid(session.getOpenid());
                    user1.setUuid(wxUser.getUuid());
                    user1.setAvatar("../../static/me_selected.png");
                    user1.setNickname(wxUser.getNickname());
                    user1.setCardImg("/static/nocard.png");
                    userService.save(user1);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("token", newToken);
                    user1.setNull();
                    map.put("user", user1);
                    return R.success(map, "首次登录，注册成功");
                }
            } catch (WxErrorException e) {
                log.error(e.getMessage(), e);
                return R.error(e.toString());
            } finally {
                WxMaConfigHolder.remove();//清理ThreadLocal
            }
        } else {
            // token wxUser 都不为空 代表已登录(token有可能过期了，要做个判断)
            String nickname = wxUser.getNickname();
            if (StringUtils.isNotBlank(nickname)) {
                QueryWrapper<User> wrapper = new QueryWrapper<>();
                wrapper.eq("openid", wxUser.getOpenid());
                User user = userService.getOne(wrapper);
                // 更新缓存
                wxAuthService.setWxUser(wxUser);
                HashMap<String, Object> map = new HashMap<>();
                map.put("token", token);
                user.setNull();
                map.put("user", user);
                return R.success(map, "登录成功");
            } else {
                // 非首次登陆但是没有授权
                wxUser.setNickname("椰叶GUEST");
                wxAuthService.setWxUser(wxUser);
                HashMap<String, Object> map = new HashMap<>();
                map.put("token", token);
                map.put("user", null);
                return R.success(map, "非首次，未授权");
            }
        }


        // 2022年10月24日前的登录授权判断
//        WxUser wxUser = null;
//        // token为空第一次登录，token不为空说明登录过
//        if (StringUtils.isNotEmpty(token)) {
//            wxUser = wxAuthService.getWxUser(token);
//        }
//        // token或者用户wxUser对象为空的时候代表需要获取openid
//        if (StringUtils.isEmpty(token) || (wxUser == null)) {
//            if (StringUtils.isBlank(code)) {
//                return R.error("res.code为空");
//            }
//            if (!wxMaService.switchover(appid)) {
//                throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
//            }
//            try {
//                WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
//                log.info(session.getSessionKey());
//                log.info(session.getOpenid());
//                wxUser = new WxUser();
//                wxUser.setOpenid(session.getOpenid());
//                QueryWrapper<User> wrapper = new QueryWrapper<>();
//                wrapper.eq("openid", session.getOpenid());
//                User user = userService.getOne(wrapper);
//                // 若用户为空，则说明没有注册过
//                if (user != null) {
//                    // 若用户Nickname不为空，说明授权过
//                    if (StringUtils.isNotEmpty(user.getNickname())) {
//                        wxUser.setNickname(user.getNickname());
//                        String newToken = wxAuthService.createToken(wxUser);
//                        HashMap<String, Object> map = new HashMap<>();
//                        map.put("token", newToken);
//                        user.setNull();
//                        map.put("user", user);
//                        return R.success(map, "查询成功");
//                    } else {
//                        // 未授权，nickname为空，但是不是第一次登录
//                        String newToken = wxAuthService.createToken(wxUser);
//                        HashMap<String, Object> map = new HashMap<>();
//                        map.put("token", newToken);
//                        map.put("user", null);
//                        return R.success(map, "未授权");
//                    }
//                } else {
//                    // 用户为空 说明首次尝试登录
//                    String newToken = wxAuthService.createToken(wxUser);
//                    User user1 = new User();
//                    user1.setOpenid(session.getOpenid());
//                    userService.save(user1);
//                    HashMap<String, Object> map = new HashMap<>();
//                    map.put("token", newToken);
//                    map.put("user", null);
//                    return R.success(map, "未登录，请授权");
//                }
//            } catch (WxErrorException e) {
//                log.error(e.getMessage(), e);
//                return R.error(e.toString());
//            } finally {
//                WxMaConfigHolder.remove();//清理ThreadLocal
//            }
//        } else {
//            // token wxUser 都不为空
//            String nickname = wxUser.getNickname();
//            if (StringUtils.isNotBlank(nickname)) {
//                QueryWrapper<User> wrapper = new QueryWrapper<>();
//                wrapper.eq("openid", wxUser.getOpenid());
//                User user = userService.getOne(wrapper);
//                // 更新缓存
//                wxAuthService.setWxUser(wxUser);
//                HashMap<String, Object> map = new HashMap<>();
//                map.put("token", token);
//                user.setNull();
//                map.put("user", user);
//                return R.success(map, "登录成功");
//            } else {
//                // 非首次登陆但是没有授权
//                wxAuthService.setWxUser(wxUser);
//                HashMap<String, Object> map = new HashMap<>();
//                map.put("token", token);
//                map.put("user", null);
//                return R.success(map, "非首次，未授权");
//            }
//        }
    }

    @PostMapping("/auth")
    public R auth(@RequestBody User user, HttpServletRequest request) {
        String token = wxAuthService.getToken(request);
        WxUser wxUser = wxAuthService.getWxUser(token);
        wxUser.setNickname(user.getNickname());
        wxAuthService.setWxUser(wxUser);
        user.setOpenid(wxUser.getOpenid());
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", wxUser.getOpenid());
        userService.update(user, wrapper);
        return R.success("授权成功", "授权成功");
    }

//    /**
//     * <pre>
//     * 获取用户信息接口
//     * </pre>
//     */
//    @GetMapping("/info")
//    public String info(@PathVariable String appid, String sessionKey,
//                       String signature, String rawData, String encryptedData, String iv) {
//        if (!wxMaService.switchover(appid)) {
//            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
//        }
//
//        // 用户信息校验
//        if (!wxMaService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
//            WxMaConfigHolder.remove();//清理ThreadLocal
//            return "user check failed";
//        }
//
//        // 解密用户信息
//        WxMaUserInfo userInfo = wxMaService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
//        WxMaConfigHolder.remove();//清理ThreadLocal
//        return JsonUtils.toJson(userInfo);
//    }

//    /**
//     * <pre>
//     * 获取用户绑定手机号信息
//     * </pre>
//     */
//    @GetMapping("/phone")
//    public String phone(@PathVariable String appid, String sessionKey, String signature,
//                        String rawData, String encryptedData, String iv) {
//        if (!wxMaService.switchover(appid)) {
//            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
//        }
//
//        // 用户信息校验
//        if (!wxMaService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
//            WxMaConfigHolder.remove();//清理ThreadLocal
//            return "user check failed";
//        }
//
//        // 解密
//        WxMaPhoneNumberInfo phoneNoInfo = wxMaService.getUserService().getPhoneNoInfo(sessionKey, encryptedData, iv);
//        WxMaConfigHolder.remove();//清理ThreadLocal
//        return JsonUtils.toJson(phoneNoInfo);
//    }

}
