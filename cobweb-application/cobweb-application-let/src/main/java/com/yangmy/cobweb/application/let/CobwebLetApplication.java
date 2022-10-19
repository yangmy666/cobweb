package com.yangmy.cobweb.application.let;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CobwebLetApplication {

    public static void main(String[] args) {
        SpringApplication.run(CobwebLetApplication.class, args);
    }

}
