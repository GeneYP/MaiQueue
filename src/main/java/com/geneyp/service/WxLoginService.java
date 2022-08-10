package com.geneyp.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.geneyp.common.Constant;
import com.geneyp.config.JwtPara;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service
public class WxLoginService {
    @Resource
    private JwtPara jwt;

    public String createToken(String uuid, String openId) {
        return JWT.create()
                .withIssuer(jwt.getIssuer())
                .withSubject("maiplayer")
                .withAudience(uuid)
                .withIssuedAt(new Date())
                .withExpiresAt(DateUtils.addDays(new Date(), jwt.getExpiresAt()))
                .withClaim("uuid", uuid)
                .withClaim("openid", openId)
//                .withClaim(Constant.ROLE, role)
                .sign(Algorithm.HMAC512(jwt.getIssuer() + uuid));
    }

    public boolean expireToken(String token, String uuid) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC512(jwt.getIssuer() + uuid)).build();
        DecodedJWT jwt = null;
        try {
            jwt = verifier.verify(token);
        } catch (JWTVerificationException e) {
            e.printStackTrace();
            return true;
        }
        Date expiresAt = jwt.getExpiresAt();
        return expiresAt.before(new Date());
    }

    public String getAudience(String token) {
        return JWT.decode(token).getAudience().get(0);
    }

//    // 获取sub
//    public String getRole(String token) {
//        return JWT.decode(token).getSubject();
//    }

    public Claim getClaimByName(String token, String name) {
        return JWT.decode(token).getClaim(name);
    }
}
