package xyz.bycor.framework.redis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import xyz.bycor.framework.redis.config.key.RedisEnum;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@ConfigurationProperties(prefix = "wolf2w.sms")
public class RedisJsonService {


    //@Value("${wolf2w.sms.limitKey}")
   // private String ssmLimitKey;

    // @Value("${wolf2w.sms.codeKey}")
    // private String ssmCodeKey;

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

    public void sAddAll(RedisEnum redisEnum, Object[] blackList, String... suffix) {
        redisTemplate.opsForSet().add(redisEnum.join(suffix), blackList);
    }

    public Set<Object> members(RedisEnum redisEnum, String... suffix) {
        return redisTemplate.opsForSet().members(redisEnum.join(suffix));
    }

    public <T> T hget(RedisEnum keyPrefixEnum, String hashKey, String... suffix) {

        return (T) redisTemplate.opsForHash().get(keyPrefixEnum.join(suffix), hashKey);
    }

    public void hRemove(RedisEnum redisEnum,Object[] array,String... suffix) {
        redisTemplate.opsForSet().remove(redisEnum.join(suffix),array);

    }

    public void hSetAll(RedisEnum redisEnum, String applicationName, Object value, String... suffix) {
        redisTemplate.opsForHash().put(redisEnum.join(suffix),applicationName,value);

    }

    public void publish(RedisEnum redisEnum, String message, String... suffix) {
        redisTemplate.convertAndSend(redisEnum.join(suffix),message);
    }

    public String stringDeserializer(byte[] data) {
        return redisTemplate.getStringSerializer().deserialize(data);
    }

    public Object valueDeserializer(byte[] data) {
        return redisTemplate.getValueSerializer().deserialize(data);
    }

    public boolean exists(RedisEnum redisEnum, String... suffix) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(redisEnum.join(suffix)));
    }

    public void putAll(RedisEnum redisEnum, Map<String, Object> map, String... suffix) {
        redisTemplate.opsForHash().putAll(redisEnum.join(suffix) , map);
    }

    public void increment(RedisEnum redisEnum, String field, Integer value,String... suffix) {
        redisTemplate.opsForHash().increment(redisEnum.join(suffix), field, value);

    }
}