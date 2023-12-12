package xyz.bycor.core.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import xyz.bycor.core.exception.BusinessException;

import java.util.Date;

public class JwtUtils {

    private static String privateKey;

    public static String getSubject() {
       //  获取privateKey
        if (StringUtils.isEmpty(privateKey)) {
            Environment environment = SpringContextUtil.getBean(Environment.class);
            privateKey = environment.getProperty("trip.auth.private-key");
        }
        // 获取token
        String token = getToken();

        // 解析token
        Claims claims = null;
        try {
            claims = parseToken(token, privateKey);
        } catch (Exception e) {

            return null;
        }

        // 获取用户名
        return claims.getSubject();
    }

    private static String getToken() {
        // 获取请求头中的token
        String token = RequestUtil.getRequest().getHeader("Authorization");
        if (StringUtils.isEmpty(token)) {
            throw new BusinessException(403, "token不能为空");
        }
        return token;
    }


    /**
     * 生成token
     *
     * @param username
     * @param expireTime
     * @param privateKey
     * @return
     */
    public static String genToken(String username, Long expireTime, String privateKey) {
        Claims claims = new DefaultClaims();
        // 关联用户名
        claims.setSubject(username);
        return Jwts.builder()
                .addClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + (expireTime * 60 * 1000)))
                .signWith(SignatureAlgorithm.HS256, privateKey)
                .compact();
    }

    public static Claims parseToken(String token, String privateKey) {
        return Jwts.parser()
                .setSigningKey(privateKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
