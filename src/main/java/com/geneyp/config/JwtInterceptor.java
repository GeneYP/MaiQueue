package com.geneyp.config;

import com.geneyp.service.WxLoginService;
import com.geneyp.util.LoginUtil;
import com.geneyp.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author GeneYP
 * @version 1.0
 * @date 2022/7/22 04:57
 * @description com.geneyp.config
 */
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {
    @Resource
    private WxLoginService wxLoginService;
//    private JWTService jwtService;
    @Resource
    private RedisUtils redisUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) {
        String token = request.getHeader("authorization");
        log.info("web.jwt authorization:{}",token);
//        if (!(obj instanceof HandlerMethod)) {
//            return true;
//        }
        if (token == null || token.isEmpty()) {
            LoginUtil.loginRequired(response);
            return false;
        }
        String userId;
        try {
            userId = wxLoginService.getAudience(token);
            String openid = wxLoginService.getClaimByName(token, "openid").asString();
            String uuid = wxLoginService.getClaimByName(token, "uuid").asString();
            String tokenKey = "token:"+ uuid +":" + openid;
            Map<Object, Object> hmget = redisUtils.hmget(tokenKey);
            String token_openid = (String) hmget.get("openid");
            if(!redisUtils.hasKey(tokenKey) || !openid.equals(token_openid)){
                LoginUtil.loginRequired(response);
                return false;
            }
        }catch (Exception e){
            log.error("jwtService 获取token失败{}",e.getMessage());
            LoginUtil.loginRequired(response);
            return false;
        }
        // 验证 token 是否过期
        boolean b = wxLoginService.expireToken(token, userId);
        if (b) {
            // 过期以后删除redis让用户重新登录
            String openid = wxLoginService.getClaimByName(token, "openid").asString();
            String uuid = wxLoginService.getClaimByName(token, "uuid").asString();
            String tokenKey = "token:"+ uuid +":" + openid;
            redisUtils.del(tokenKey);
            LoginUtil.loginRequired(response);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }


}
