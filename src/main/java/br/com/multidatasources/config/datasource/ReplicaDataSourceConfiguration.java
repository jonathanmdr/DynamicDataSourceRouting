package br.com.multidatasources.config.datasource;

import br.com.multidatasources.config.properties.datasource.DatabaseConnectionProperties;
import br.com.multidatasources.config.properties.datasource.ReplicaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

import static br.com.multidatasources.config.datasource.DataSourceType.READ_ONLY;

@Configuration
public class ReplicaDataSourceConfiguration implements DataSourceConfiguration {

    @Override
    public String getPoolName() {
        return READ_ONLY.getPoolName();
    }

    @Override
    public int getMinimumIdle() {
        return READ_ONLY.getMinimumIdle();
    }

    @Override
    public int getMaximumPoolSize() {
        return READ_ONLY.getMaximumPoolSize();
    }

    @Override
    public long getConnectionTimeout() {
        return READ_ONLY.getConnectionTimeout();
    }

    @Override
    public long getMaxLifetime() {
        return READ_ONLY.getMaxLifetime();
    }

    @Bean
    @ReplicaDataSource
    public DataSource replicaDataSource(@ReplicaProperties final DatabaseConnectionProperties properties) {
        final var dataSource = new DriverManagerDataSource();

        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());

        return definePoolDataSourceConnection(dataSource);
    }

}
