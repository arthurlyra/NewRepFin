package com.ea975.repfin.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.ea975.repfin")
@EnableJpaRepositories(basePackages = "com.ea975.repfin.daos")
@EntityScan("com.ea975.repfin.components")
public class Main {

    public static void main(String[] args){
        SpringApplication.run(Main.class, args);
    }

}
