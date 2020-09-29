package com.itheima.health;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @Author: zengrui
 * @Date: 2020/9/22 18:03
 */
public class JobApplication {
    public static void main(String[] args) throws IOException {
        new ClassPathXmlApplicationContext("classpath:spring-job.xml");
        System.in.read();
    }
}
