package com.geneyp.service;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.Claim;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.geneyp.common.Constant;
import com.geneyp.config.JwtPara;
import com.geneyp.dao.UserMapper;
import com.geneyp.entity.User;
import com.geneyp.exception.ApiException;
import com.geneyp.service.WxLoginService;
import com.geneyp.util.IdCreater;
import com.geneyp.util.RedisUtils;
import com.geneyp.vo.WxUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class WxAuthService {
    @Resource
    private UserMapper userMapper;
    @Resource
    WxLoginService wxLoginService;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private JwtPara jwt;

    /**
     * 根据token获取用户
     *
     * @param token 传入token
     * @return wxUser
     */
    public WxUser getWxUser(String token) {
        if (StringUtils.isBlank(token)) {
            throw new ApiException("无认证信息");
        }
        String uuid = wxLoginService.getAudience(token);
        Claim claim = wxLoginService.getClaimByName(token, "openid");
        // 必须通过asString转换成String类型，否则强转会导致结果附带双引号
        String openId = claim.asString();
        if (StringUtils.isNoneBlank(uuid, openId)) {
            WxUser wxUser = new WxUser();
            wxUser.setUuid(uuid);
            wxUser.setOpenid(openId);
            Map<Object, Object> hmget = redisUtils.hmget("token:" + uuid + ":" + openId);
            if (StringUtils.isEmpty((String) hmget.get("nickname"))) {
                wxUser = null;
            } else {
                wxUser.setNickname((String) hmget.get("nickname"));
            }
            return wxUser;
        }
        return null;
    }

    public String createToken(WxUser wxUser) {
        String token = wxLoginService.createToken(wxUser.getUuid(), wxUser.getOpenid());
//        String redisKey = "token:" + wxUser.getUuid() + ":" + wxUser.getOpenid();
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("uuid", wxUser.getUuid());
//        map.put("openid", wxUser.getOpenid());
//        map.put("nickname", wxUser.getNickname());
//        redisUtils.hmset(redisKey, map);
        String redisKey = setRedis(wxUser);
        redisUtils.expire(redisKey, jwt.getExpiresAt(), TimeUnit.DAYS);
        return token;
    }

    public void setWxUser(WxUser wxUser) {
        if (StringUtils.isNotEmpty(wxUser.getUuid())) {
//            String redisKey = "token:" + wxUser.getUuid() + ":" + wxUser.getOpenid();
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("uuid", wxUser.getUuid());
//            map.put("openid", wxUser.getOpenid());
//            map.put("nickname", wxUser.getNickname());
//            redisUtils.hmset(redisKey, map);
            String redisKey = setRedis(wxUser);
            redisUtils.expire(redisKey, jwt.getExpiresAt(), TimeUnit.DAYS);
        }
    }

    public String getToken(HttpServletRequest request) {
        return request.getHeader("authorization");
    }

    public String setRedis(WxUser wxUser) {
        String redisKey = "token:" + wxUser.getUuid() + ":" + wxUser.getOpenid();
        HashMap<String, Object> map = new HashMap<>();
        map.put("uuid", wxUser.getUuid());
        map.put("openid", wxUser.getOpenid());
        map.put("nickname", wxUser.getNickname());
        redisUtils.hmset(redisKey, map);
        return redisKey;
    }

    public void clearRedis(WxUser wxUser) {
        if (StringUtils.isNotEmpty(wxUser.getUuid())) {
            String redisKey = "token:" + wxUser.getUuid() + ":" + wxUser.getOpenid();
            redisUtils.del(redisKey);
        }
    }


//    /**
//     * 退出
//     *
//     * @param request
//     */
//    public void fcLogout(HttpServletRequest request) {
//        String token = request.getHeader(Constant.TOKEN_HEADER);
//        if (StringUtils.isBlank(token)) {
//            throw new ApiException("无认证信息");
//        }
//        String userId = jwtService.getAudience(token);
//        String role = jwtService.getRole(token);
//        if (StringUtils.isNoneBlank(userId, role)) {
//            redisUtils.del("token:" + role + ":" + userId);
//        }
//    }
//
//    /**
//     * 管系统认证
//     *
//     * @param authBean
//     * @return
//     */
//    public String manageLogin(AuthBean authBean) {
//        QueryWrapper<FcSysUser> wrapper = new QueryWrapper<>();
//        wrapper.lambda().eq(FcSysUser::getUsername, authBean.getUsername());
//        List<FcSysUser> sysUsers = fcSysUserMapper.selectList(wrapper);
//        if (sysUsers == null || sysUsers.isEmpty()) {
//            throw new ApiException("用户名或密码错误");
//        }
//        FcSysUser fcSysUser = sysUsers.get(0);
//        if (!authBean.getPassword().equals(fcSysUser.getPassword())) {
//            throw new ApiException("用户名或密码错误");
//        }
//        RoleEnum roleEnum = fcSysUser.getRole() == 0 ? RoleEnum.MANAGER : RoleEnum.MERCHANT;
//        String token = jwtService.createToken(fcSysUser.getUserId() + "", roleEnum.name());
//        String redisKey = "token:" + roleEnum.name() + ":" + fcSysUser.getUserId();
//        redisUtils.set(redisKey, token);
//        redisUtils.expire(redisKey, jwt.getExpiresAt(), TimeUnit.DAYS);
//        return token;
//    }
//
//    /**
//     * 退出
//     *
//     * @param request
//     */
//    public void manageLogout(HttpServletRequest request) {
//        String token = request.getHeader(Constant.TOKEN_HEADER);
//        if (StringUtils.isBlank(token)) {
//            throw new ApiException("无认证信息");
//        }
//        String userId = jwtService.getAudience(token);
//        String role = jwtService.getRole(token);
//        if (StringUtils.isNoneBlank(userId, role)) {
//            redisUtils.del("token:" + role + ":" + userId);
//        }
//    }
//
//    /**
//     * 获取用户信息
//     *
//     * @param request
//     * @return
//     */
//    public JSONObject getUserInfo(HttpServletRequest request) {
//        String token = request.getHeader(Constant.TOKEN_HEADER);
//        if (StringUtils.isBlank(token)) {
//            throw new ApiException("无认证信息");
//        }
//        String userId = jwtService.getAudience(token);
//        FcSysUser fcSysUser = fcSysUserMapper.selectById(userId);
//        String role = jwtService.getRole(token);
//        if (StringUtils.isNoneBlank(userId, role)) {
//            JSONObject json = new JSONObject();
//            json.put("userId", userId);
//            json.put("role", role);
//            json.put("username", fcSysUser.getUsername());
//            return json;
//        }
//        return null;
//    }
//
//    /**
//     * 获取当前用户ID
//     * @param request
//     * @return
//     */
//    public String getUserId(HttpServletRequest request){
//        String token = request.getHeader(Constant.TOKEN_HEADER);
//        if (StringUtils.isBlank(token)) {
//            throw new ApiException("无认证信息");
//        }
//        return jwtService.getAudience(token);
//    }
}
