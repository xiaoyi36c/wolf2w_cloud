package xyz.bycor.member.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
import xyz.bycor.member.vo.RegisterRequestUserinfo;

import java.util.Map;

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
    public R<Map<String, Object>> doLogin(LoginRequestUserinfo userinfo) {
        Userinfo user = this.getPhone(userinfo.getPhone());
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


        // 5. 存入redis

        // 6.  存入map,将token和用户信息一起返回


        return null;
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




