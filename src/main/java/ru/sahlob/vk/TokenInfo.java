package ru.sahlob.vk;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.yaml")
@Getter
public class TokenInfo {


    public static String geoserverUrl;

    @Value("${notToken}")
    public String url;

    public TokenInfo() {
        geoserverUrl = getUrl();
    }

}
