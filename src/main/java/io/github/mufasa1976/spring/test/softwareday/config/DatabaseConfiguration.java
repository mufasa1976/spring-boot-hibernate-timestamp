package io.github.mufasa1976.spring.test.softwareday.config;

import io.github.mufasa1976.spring.test.softwareday.SpringBootTestSoftwaredayApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDateTime;
import java.util.Optional;

@Configuration
@EnableJpaRepositories(basePackageClasses = SpringBootTestSoftwaredayApplication.class)
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
public class DatabaseConfiguration {
  @Bean
  @ConditionalOnMissingBean
  public DateTimeProvider dateTimeProvider() {
    return () -> Optional.of(LocalDateTime.now());
  }
}
