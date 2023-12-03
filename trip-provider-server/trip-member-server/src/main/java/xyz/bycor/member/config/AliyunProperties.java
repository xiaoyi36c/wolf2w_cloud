package xyz.bycor.member.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
@Getter
@Setter
@ConfigurationProperties(prefix = "aliyun")
public class AliyunProperties {
    private AccessKey accessKey;
    private Sms sms;

    @Getter
    @Setter
    public static class AccessKey {
        private String id;
        private String secret;

    }


    @Getter
    @Setter
    public static class Sms{
        private String endpoint;
        private String sign;

        private Map<String,String> templateMap;

    }
}
