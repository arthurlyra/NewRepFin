package com.ea975.repfin.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.ea975")
public class Main {

    public static void main(String[] args){
        SpringApplication.run(Main.class, args);
    }

}
