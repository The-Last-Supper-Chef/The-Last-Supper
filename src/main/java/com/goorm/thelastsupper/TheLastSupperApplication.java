package com.goorm.thelastsupper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TheLastSupperApplication {

    public static void main(String[] args) {
        SpringApplication.run(TheLastSupperApplication.class, args);
    }

}
