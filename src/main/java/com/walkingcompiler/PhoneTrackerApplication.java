package com.walkingcompiler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.walkingcompiler.repository")
public class PhoneTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(PhoneTrackerApplication.class, args);
    }
}
