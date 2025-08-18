package com.example.studentmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class StudentmanagementApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(StudentmanagementApplication.class, args);
        context.getEnvironment().setActiveProfiles("dev");
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
