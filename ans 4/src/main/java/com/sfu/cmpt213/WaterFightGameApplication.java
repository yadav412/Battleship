package com.sfu.cmpt213;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main Spring Boot application class for the Blanket Fort Game.
 * Initializes and starts the REST API server for the water fight game.
 */
@SpringBootApplication
@ComponentScan(basePackages = { "com.sfu.cmpt213", "", "controller", "dto", "model" })
public class WaterFightGameApplication {
    public static void main(String[] args) {
        SpringApplication.run(WaterFightGameApplication.class, args);
    }
}
