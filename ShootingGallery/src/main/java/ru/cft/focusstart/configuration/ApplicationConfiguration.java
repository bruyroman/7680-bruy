package ru.cft.focusstart.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath://application.properties")
@ComponentScan("ru.cft.focusstart")
public class ApplicationConfiguration {

    @Bean(destroyMethod = "close")
    public HikariDataSource dataSource(
            @Value("${dataSource.url}") String url,
            @Value("${dataSource.userName}") String userName,
            @Value("${dataSource.password}") String password,
            @Value("${dataSource.maximumPoolSize}") int maximumPoolSize
    ) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(userName);
        config.setPassword(password);
        config.setMaximumPoolSize(maximumPoolSize);
        config.setDriverClassName("org.postgresql.Driver");

        return new HikariDataSource(config);
    }
}
