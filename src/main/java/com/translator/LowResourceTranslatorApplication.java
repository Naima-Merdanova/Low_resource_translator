package com.translator;

import com.translator.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableConfigurationProperties(AppProperties.class)
@SpringBootApplication
public class LowResourceTranslatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(LowResourceTranslatorApplication.class, args);
    }
}