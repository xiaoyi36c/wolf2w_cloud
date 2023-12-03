package xyz.bycor.member.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AliyunProperties.class)
public class AliyunConfig {

    /**
     * 使用AK&SK初始化账号Client
     * @param aliyunProperties 阿里云配置
     * @return Client
     * @throws Exception
     */
    @Bean
    public  com.aliyun.dysmsapi20170525.Client createClient(AliyunProperties aliyunProperties) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(aliyunProperties.getAccessKey().getId())
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(aliyunProperties.getAccessKey().getSecret());
        // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
        config.endpoint = aliyunProperties.getSms().getEndpoint();
        return new com.aliyun.dysmsapi20170525.Client(config);
    }
}
