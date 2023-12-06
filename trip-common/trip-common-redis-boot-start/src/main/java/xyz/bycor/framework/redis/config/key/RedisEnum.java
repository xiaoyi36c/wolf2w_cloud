package xyz.bycor.framework.redis.config.key;

import lombok.Getter;

import java.util.concurrent.TimeUnit;
@Getter
public enum RedisEnum {



    SMS_REGISTER_LIMIT_KEY("wolf2w:sms:limit:key:", 5L, TimeUnit.MINUTES),

    SMS_REGISTER_CODE_KEY("wolf2w:sms:code:key:", 30L, TimeUnit.MINUTES),
    USER_LOGIN_INFO_KEY("wolf2w:login:info:key:", 30L, TimeUnit.MINUTES),
    AUTH_BLACKLIST("wolf2w:auth:black_list"),
    AUTH_LAST_BLACKLIST("wolf2w:auth:last_black_list"),
    AUTH_BLACKLIST_CHANNEL("wolf2w:auth:black_list:channel");


    private String prefix;
    private Long timeOut =-1L;

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
