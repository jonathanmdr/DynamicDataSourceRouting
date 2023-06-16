package br.com.multidatasources.config.datasource;

import br.com.multidatasources.config.properties.datasource.DatabaseConnectionProperties;
import br.com.multidatasources.config.properties.datasource.MasterProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

import static br.com.multidatasources.config.datasource.DataSourceType.READ_WRITE;

@Configuration
public class MasterDataSourceConfiguration implements DataSourceConfiguration {

    @Override
    public String getPoolName() {
        return READ_WRITE.getPoolName();
    }

    @Override
    public int getMinimumIdle() {
        return READ_WRITE.getMinimumIdle();
    }

    @Override
    public int getMaximumPoolSize() {
        return READ_WRITE.getMaximumPoolSize();
    }

    @Override
    public long getConnectionTimeout() {
        return READ_WRITE.getConnectionTimeout();
    }

    @Override
    public long getMaxLifetime() {
        return READ_WRITE.getMaxLifetime();
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
