package com.tigger.drawing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class DrawingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DrawingApplication.class, args);
        log.info("##########DrawingApplication start!");
    }
}
