package com.robot;

import love.forte.simboot.spring.autoconfigure.EnableSimbot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hanse*
 * @date 2022/11/28 9:34
 */
@EnableSimbot
@SpringBootApplication
public class RobotApplication {

    public static void main(String[] args) {

        SpringApplication.run(RobotApplication.class,args);
    }
}
