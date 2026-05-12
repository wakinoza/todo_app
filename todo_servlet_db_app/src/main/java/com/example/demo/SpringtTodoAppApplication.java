package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class SpringtTodoAppApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringtTodoAppApplication.class, args);
  }

}
