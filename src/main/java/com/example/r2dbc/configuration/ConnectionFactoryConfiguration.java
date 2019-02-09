package com.example.r2dbc.configuration;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.function.DatabaseClient;

@Configuration
public class ConnectionFactoryConfiguration {

    @Bean
    public ConnectionFactory connectionFactory(){

        PostgresqlConnectionConfiguration configuration =
                PostgresqlConnectionConfiguration.builder()
                        .host("localhost")
                        .database("postgres")
                        .username("postgres")
                        .password("")
                        .port(32768)
                        .build();

        return new PostgresqlConnectionFactory(configuration);
    }

    @Bean
    public DatabaseClient databaseClient(){
        return DatabaseClient.create(connectionFactory());
    }
}
