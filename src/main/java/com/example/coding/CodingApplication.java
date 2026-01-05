package com.example.coding;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.coding.mapper")
public class CodingApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodingApplication.class, args);
    }
}
