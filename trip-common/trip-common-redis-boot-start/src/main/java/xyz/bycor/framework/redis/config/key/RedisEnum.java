package xyz.bycor.framework.redis.config.key;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;
@Getter
public enum RedisEnum {

    SMS_REGISTER_LIMIT_KEY("wolf2w:sms:limit:key:", 5L, TimeUnit.MINUTES),

    SMS_REGISTER_CODE_KEY("wolf2w:sms:code:key:", 30L, TimeUnit.MINUTES),
    USER_LOGIN_INFO_KEY("wolf2w:login:info:key:", 30L, TimeUnit.MINUTES),
    AUTH_BLACKLIST("wolf2w:auth:black_list"),
    AUTH_LAST_BLACKLIST("wolf2w:auth:last_black_list"),
    AUTH_BLACKLIST_CHANNEL("wolf2w:auth:black_list:channel"),
    STRATEGY_STAT_DATA_HASH("wolf2w:article:stat:data:hash:"),
    STRATEGY_THUMBS_RECORD_STRING("wolf2w:articles:strategies:thumbs:record"),
    STRATEGY_STAT_DATA_ZSET("wolf2w:article:stat:data:zset");


    private String prefix;
    @Setter
    private Long timeOut =-1L;
    @Setter
    private TimeUnit timeUnit;

    RedisEnum(String prefix) {
        this(prefix, -1L, null);
    }

    RedisEnum(String prefix, Long timeOut, TimeUnit timeUnit) {
        this.prefix = prefix;
        this.timeOut = timeOut;
        this.timeUnit = timeUnit;
    }

    public String join(String... suffix) {
        StringBuilder sb = new StringBuilder(prefix);
        sb.append(prefix);
        for (String s : suffix) {
            sb.append(s);
        }
        return sb.toString();
    }

}
