package io.github.mufasa1976.spring.test.softwareday.config;

import io.github.mufasa1976.spring.test.softwareday.SpringBootTestSoftwaredayApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = SpringBootTestSoftwaredayApplication.class)
public class DatabaseConfiguration {}
