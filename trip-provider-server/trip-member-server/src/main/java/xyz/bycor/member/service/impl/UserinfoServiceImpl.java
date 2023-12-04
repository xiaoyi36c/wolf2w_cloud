package xyz.bycor.member.service.impl;


import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.bycor.core.exception.BusinessException;
import xyz.bycor.core.response.R;
import xyz.bycor.core.utils.SecurityUtils;
import xyz.bycor.framework.redis.config.RedisJsonService;
import xyz.bycor.framework.redis.config.key.RedisEnum;
import xyz.bycor.member.domain.Userinfo;
import xyz.bycor.member.mapper.UserinfoMapper;
import xyz.bycor.member.service.UserinfoService;
import xyz.bycor.member.vo.LoginRequestUserinfo;
import xyz.bycor.member.vo.LoginResponse;
import xyz.bycor.member.vo.LoginResponseUser;
import xyz.bycor.member.vo.RegisterRequestUserinfo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author BAI
 * @description 针对表【userinfo】的数据库操作Service实现
 * @createDate 2023-12-02 15:57:08
 */
@Service
public class UserinfoServiceImpl extends ServiceImpl<UserinfoMapper, Userinfo>
        implements UserinfoService {

    private final RedisJsonService redisJsonService;

    public UserinfoServiceImpl(RedisJsonService redisJsonService) {
        this.redisJsonService = redisJsonService;
    }

    @Override
    public Userinfo getPhone(String phone) {
        return super.getOne(new QueryWrapper<Userinfo>().lambda().eq(Userinfo::getPhone, phone));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doRegister(RegisterRequestUserinfo userinfo) {
        // 1.判断手机号是否已经注册
        LambdaQueryWrapper<Userinfo> eq = new QueryWrapper<Userinfo>().lambda().eq(Userinfo::getPhone, userinfo.getPhone())
                .last("for update"); // 考虑并发问题,加锁
        Userinfo phone = super.getOne(eq);
        if (phone != null) {
            throw new BusinessException("手机号已经注册,请直接登录");
        }
        // 2.判断验证码是否正确
        String verifyCode = redisJsonService.get(RedisEnum.SMS_REGISTER_CODE_KEY, userinfo.getPhone());
        if (!userinfo.getVerifyCode().equals(verifyCode)) {
            throw new BusinessException(400, "验证码错误");
        }
        redisJsonService.delete(RedisEnum.SMS_REGISTER_CODE_KEY, userinfo.getPhone());
        // 3.加密密码  // BCryptPasswordEncoder
        String encodePassword = SecurityUtils.encodePassword(userinfo.getPassword());
        userinfo.setPassword(encodePassword);
        // 4.Vo=>Po
        Userinfo userinfo1 = this.buildUserInfo(userinfo);
        // 5.保存
        super.save(userinfo1);

    }


/**
 * 登录
 *
 * @param userinfo
 * @return
 */
    @Override
    public R<LoginResponse> doLogin(LoginRequestUserinfo userinfo) {
        Userinfo user = this.getPhone(userinfo.getUsername());
        // 1.判断手机号是否注册
        if (user == null) {
            throw new BusinessException("手机号未注册");
        }
        // 2.判断密码是否正确
        boolean b = SecurityUtils.matchesPassword(userinfo.getPassword(), user.getPassword());
        if (!b) {
            throw new BusinessException("密码错误");
        }
        // 3.判断用户是否被禁用
        if (user.getState() == Userinfo.STATE_DISABLE) {
            throw new BusinessException("用户已被禁用");
        }
        // 4. 生成JWT token
        Map<String, Object> map = new HashMap<String, Object>() {
            {
                put("uid", user.getId());
                put("expire_time", System.currentTimeMillis() + 1000 * 60 * 60 * 24);
            }
        };

        String secretToken = this.genToken(user.getPhone(), 60L);
        String refreshToken = this.genToken(user.getPhone(), 120L);
        // 5.  用户信息存入redis
        LoginResponseUser loginResponseUser = new LoginResponseUser();
        BeanUtils.copyProperties(user, loginResponseUser);
        redisJsonService.set(RedisEnum.USER_LOGIN_INFO_KEY, user, user.getPhone());
        // 6.  封装返回结果
        LoginResponse loginResponse = new LoginResponse(secretToken, refreshToken, loginResponseUser);

        return R.ok(loginResponse);
    }

    /**
     * 刷新token
     *
     * @param refreshToken
     * @return AccessToken,RefreshToken
     */
    @Override
    public R<JSONObject> refreshToken(String refreshToken) {
        try {
            // 1. 解析tokoen是否有效
            Claims claims = Jwts.parser()
                    .setSigningKey("wolfcode")
                    .parseClaimsJws(refreshToken)
                    .getBody();
            // 2. 创建新的 token
            String accessToken = genToken(claims.getSubject(), 10L);// 新的 accessToken
            refreshToken = genToken(claims.getSubject(), 30L);// 新的 refreshToken
            // 3. 刷新 redis 中的用户信息为新的 30 分钟
            redisJsonService.expire(RedisEnum.USER_LOGIN_INFO_KEY, 30L, TimeUnit.MINUTES, claims.getSubject());

            // 4. 封装响应对象
            JSONObject json = new JSONObject();
            json.put("accessToken", accessToken);
            json.put("refreshToken", refreshToken);
            return R.ok(json);
        } catch (Exception e) {
            log.error("[刷新token] 解析refreshToken失败", e);
            throw new BusinessException(401, "refreshToken已失效");
        }
    }

    /**
     * 生成token
     *
     * @param username
     * @param expireTime
     * @return
     */
    private String genToken(String username, Long expireTime) {
        Claims claims = new DefaultClaims();
        // 关联用户名
        claims.setSubject(username);
        return Jwts.builder()
                .addClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + (expireTime * 60 * 1000)))
                .signWith(SignatureAlgorithm.HS256, "wolfcode")
                .compact();
    }




    /**
     * 构建用户信息
     *
     * @param registerRequestUserinfo
     * @return Userinfo
     */
    private Userinfo buildUserInfo(RegisterRequestUserinfo registerRequestUserinfo) {
        Userinfo userinfo = new Userinfo();
        userinfo.setPhone(registerRequestUserinfo.getPhone());
        userinfo.setPassword(registerRequestUserinfo.getPassword());
        userinfo.setNickname(registerRequestUserinfo.getNickname());
        userinfo.setState(Userinfo.STATE_NORMAL);
        return userinfo;
    }
}




