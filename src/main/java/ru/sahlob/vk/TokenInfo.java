package ru.sahlob.vk;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.yaml")
@Getter
public class TokenInfo {

    @Value("${notToken}")
    private String geoserverUrl;
}
