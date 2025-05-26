package com.happypill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class HappypillApplication {

    public static void main(String[] args) {
        SpringApplication.run(HappypillApplication.class, args);
    }

}
