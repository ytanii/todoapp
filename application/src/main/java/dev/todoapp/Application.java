package dev.todoapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "dev.todoapp")
public class Application{
    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }
}