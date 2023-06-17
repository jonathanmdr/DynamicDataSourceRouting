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
    public String poolName() {
        return READ_ONLY.poolName();
    }

    @Override
    public int minimumIdle() {
        return READ_ONLY.minimumIdle();
    }

    @Override
    public int maximumPoolSize() {
        return READ_ONLY.maximumPoolSize();
    }

    @Override
    public long connectionTimeout() {
        return READ_ONLY.connectionTimeout();
    }

    @Override
    public long idleTimeout() {
        return READ_ONLY.idleTimeout();
    }

    @Override
    public long maxLifetime() {
        return READ_ONLY.maxLifetime();
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
