package com.cs316.SimpleAdminSys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@EntityScan("com.cs316.SimpleAdminSys")
//@EnableJpaRepositories("com.cs316.SimpleAdminSys.api")
@SpringBootApplication()
public class SimpleAdminSys extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SimpleAdminSys.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }
}

