package com.li.oop;

import com.li.oop.entity.Application;
import com.li.oop.repository.ApplicationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OopApplication {

    private  static final Logger log = LoggerFactory.getLogger(OopApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(OopApplication.class, args);
    }
}
