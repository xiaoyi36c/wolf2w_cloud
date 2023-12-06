package xyz.bycor.core.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;

import java.util.Date;

public class JwtUtils {

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
