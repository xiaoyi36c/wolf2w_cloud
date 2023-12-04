package xyz.bycor.framework.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import xyz.bycor.framework.redis.config.key.RedisEnum;

import java.util.concurrent.TimeUnit;

@ConfigurationProperties(prefix = "wolf2w.sms")
public class RedisJsonService {


    @Value("${wolf2w.sms.limitKey}")
    private String ssmLimitKey;

    @Value("${wolf2w.sms.codeKey}")
    private String ssmCodeKey;

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisJsonService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean setIfAbsent(RedisEnum keyPrefixEnum, Object value, String... suffix) {
        String fullKey = keyPrefixEnum.join(suffix);
        Boolean ret;
        if (keyPrefixEnum.getTimeOut() > 0) {
            ret = redisTemplate.opsForValue().setIfAbsent(fullKey, value, keyPrefixEnum.getTimeOut(), keyPrefixEnum.getTimeUnit());
        } else {
            ret = redisTemplate.opsForValue().setIfAbsent(fullKey, value);
        }
        return ret != null && ret;
    }

    public void set(RedisEnum keyPrefixEnum, Object value, String... suffix) {
        String fullKey = keyPrefixEnum.join(suffix);
        if (keyPrefixEnum.getTimeOut() > 0) {
            redisTemplate.opsForValue().set(fullKey, value, keyPrefixEnum.getTimeOut(), keyPrefixEnum.getTimeUnit());
        } else {
            redisTemplate.opsForValue().set(fullKey, value);
        }
    }

    public void setSsmLimitKey( Object value,long timeOut, TimeUnit timeUnit) {
        String key = ssmLimitKey + value;
       redisTemplate.opsForValue().set(key, value, timeOut, timeUnit);
    }

    public void setIfAbsentLimitKey(Object value, long timeOut, TimeUnit timeUnit) {
        String key = ssmLimitKey + value;
        redisTemplate.opsForValue().set(key, value, timeOut, timeUnit);
    }


    public void setCodeKey( String phone,Object code,long timeOut, TimeUnit timeUnit) {
        String key = ssmCodeKey + phone;
        redisTemplate.opsForValue().set(key, code, timeOut, timeUnit);
    }

    public void setIfAbsentCodeKey(String phone,Object code, long timeOut, TimeUnit timeUnit) {
        String key = ssmCodeKey + code;
        redisTemplate.opsForValue().set(key, code, timeOut, timeUnit);
    }

    public void delete(RedisEnum redisEnum, String phone) {
        String key = redisEnum.join(phone);
        redisTemplate.delete(key);
    }

    public <T>T get(RedisEnum redisEnum, String ...suffix) {
        Object o = redisTemplate.opsForValue().get(redisEnum.join(suffix));
        return (T) o;
    }

    /**
     * 设置过期时间
     * @param redisEnum 枚举
     * @param timeOut 过期时间
     * @param timeUnit 时间单位
     * @param suffix key后缀
     */
    public void expire(RedisEnum redisEnum, long timeOut, TimeUnit timeUnit, String ...suffix) {
        String key = redisEnum.join(suffix);
        redisTemplate.expire(key, timeOut, timeUnit);
    }
}