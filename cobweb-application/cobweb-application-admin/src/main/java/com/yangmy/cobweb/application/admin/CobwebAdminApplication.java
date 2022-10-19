package com.yangmy.cobweb.application.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CobwebAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(CobwebAdminApplication.class, args);
    }

}
