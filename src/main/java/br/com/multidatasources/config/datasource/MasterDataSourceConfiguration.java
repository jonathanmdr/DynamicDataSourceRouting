package br.com.multidatasources.config.datasource;

import br.com.multidatasources.config.properties.datasource.DatabaseConnectionProperties;
import br.com.multidatasources.config.properties.datasource.MasterProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

import static br.com.multidatasources.config.datasource.DataSourceType.READ_ONLY;
import static br.com.multidatasources.config.datasource.DataSourceType.READ_WRITE;

@Configuration
public class MasterDataSourceConfiguration implements DataSourceConfiguration {

    @Override
    public String poolName() {
        return READ_WRITE.poolName();
    }

    @Override
    public int minimumIdle() {
        return READ_WRITE.minimumIdle();
    }

    @Override
    public int maximumPoolSize() {
        return READ_WRITE.maximumPoolSize();
    }

    @Override
    public long idleTimeout() {
        return READ_ONLY.idleTimeout();
    }

    @Override
    public long connectionTimeout() {
        return READ_WRITE.connectionTimeout();
    }

    @Override
    public long maxLifetime() {
        return READ_WRITE.maxLifetime();
    }

    @Bean
    @MasterDataSource
    public DataSource masterDataSource(@MasterProperties final DatabaseConnectionProperties properties) {
        final var dataSource = new DriverManagerDataSource();

        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());

        return definePoolDataSourceConnection(dataSource);
    }

}
