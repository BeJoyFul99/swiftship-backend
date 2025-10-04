package com.swiftship.logistic.backend;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class LogisticApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(LogisticApplication.class);
        app.run(LogisticApplication.class, args);
    }


}
