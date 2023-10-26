package com.wanted.preonboarding.security.jwt;

import com.wanted.preonboarding.config.property.YamlPropertySourceFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("jwt")
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
public class JwtProperties {

    private String issuer;

    private String secret;

    private int accessTokenExpirationMinutes;

    private int refreshTokenExpirationMinutes;
}
