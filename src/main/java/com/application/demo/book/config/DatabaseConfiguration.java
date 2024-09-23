package com.application.demo.book.config;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories({ "com.application.demo.book.repository" })
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnableTransactionManagement
public class DatabaseConfiguration {

    @Bean(name = "bookDataSource")
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSource() {
        return new DataSourceProperties();
    }

    // creates data-source bean
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.hikari")
    public DataSource digxDataSource() {
        return dataSource().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }
}
