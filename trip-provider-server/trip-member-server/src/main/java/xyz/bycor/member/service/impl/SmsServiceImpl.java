package xyz.bycor.member.service.impl;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.tea.TeaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import xyz.bycor.core.exception.BusinessException;
import xyz.bycor.framework.redis.config.RedisJsonService;
import xyz.bycor.framework.redis.config.key.RedisEnum;
import xyz.bycor.member.config.AliyunProperties;
import xyz.bycor.member.service.SmsService;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {



    private final RedisJsonService redisJsonService;

    public SmsServiceImpl(RedisJsonService redisJsonService, AliyunProperties aliyunProperties, Client client) {
        this.redisJsonService = redisJsonService;
        this.aliyunProperties = aliyunProperties;
        this.client = client;
    }

    private final AliyunProperties aliyunProperties;

    private final com.aliyun.dysmsapi20170525.Client client;




    @Override
    public void registerSend(String phone) {

        // 限制5分钟内只能发送一次
        boolean b = redisJsonService.setIfAbsent(RedisEnum.SMS_REGISTER_LIMIT_KEY, phone, phone);
        if (!b ) {
            log.error("【发送短信验证码】5分钟内只能发送一次，手机号：{}", phone);
            throw new BusinessException("发送短信验证码过于频繁，请稍后再试");
        }

        // 生成验证码
        String verifyCode = genVerifyCode(6);
        log.info("验证码为：{}", verifyCode);
        // 发送验证码
        try {

        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                .setSignName(aliyunProperties.getSms().getSign())
                .setTemplateCode(aliyunProperties.getSms().getTemplateMap().get("register"))
                .setPhoneNumbers(phone)
                .setTemplateParam("{\"code\":\""+verifyCode+"\"}");
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();

            // 复制代码运行请自行打印 API 的返回值
            SendSmsResponse response = client.sendSmsWithOptions(sendSmsRequest, runtime);
            String code = response.getBody().getCode();
            if (!"OK".equalsIgnoreCase(code)) {
                log.error("【发送短信验证码】发送短信验证码失败，手机号：{}，原因：{}", phone, response.getBody().getMessage());
                //  删除限制
                redisJsonService.delete(RedisEnum.SMS_REGISTER_LIMIT_KEY,phone);
                throw new BusinessException("发送短信验证码失败，请稍后再试");
            }
        }catch (Exception error) {
           log.error("【发送短信验证码】发送短信验证码失败，手机号：{}", phone);
           //  删除限制
            redisJsonService.delete(RedisEnum.SMS_REGISTER_LIMIT_KEY,phone);
            throw new BusinessException("发送短信验证码失败，请稍后再试");
        }
        // 保存验证码到redis 30分钟
        redisJsonService.set(RedisEnum.SMS_REGISTER_CODE_KEY,verifyCode,phone);

    }


    private String genVerifyCode(int length) {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }



}
