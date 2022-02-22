package com.winitech.cs;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class RestTemplateBuilderApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestTemplateBuilderApplication.class, args);
    }
}
