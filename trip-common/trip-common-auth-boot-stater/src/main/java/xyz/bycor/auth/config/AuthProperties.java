package xyz.bycor.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "trip.auth")
public class AuthProperties {
    private String privateKey;
    private Long refreshTokenTimeOut;

    private Long accessTokenTimeOut;


}
