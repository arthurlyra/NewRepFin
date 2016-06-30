package com.ea975.repfin.main.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.metadata.HikariDataSourcePoolMetadata;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
@ComponentScan({"com.ea975.repfin.*"})
@Import({WebSecurityConfiguration.class})
public class DatabaseConfiguration {

    @Bean(name = "dataSource")
    public HikariConfig getHikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(2);
        config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        config.addDataSourceProperty("url", "jdbc:mysql://localhost:3306/repfin");
        config.addDataSourceProperty("user", "root");
        config.addDataSourceProperty("password", "tutu2502");

        return config;
    }

    @Bean
    @Autowired
    public HikariDataSource getDataSource(HikariConfig config) {
        return new HikariDataSource(config);
    }
}