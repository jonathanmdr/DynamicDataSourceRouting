package br.com.multidatasources.config.datasource;

import br.com.multidatasources.config.properties.datasource.DatabaseConnectionProperties;
import br.com.multidatasources.config.properties.datasource.MasterProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class MasterDataSourceConfiguration implements DataSourceConfiguration {

    @Override
    public String getPoolName() {
        return DataSourceType.READ_WRITE.getPoolName();
    }

    @Override
    public int getMinimumIdle() {
        return DataSourceType.READ_WRITE.getMinimumIdle();
    }

    @Override
    public int getMaximumPoolSize() {
        return DataSourceType.READ_WRITE.getMaximumPoolSize();
    }

    @Override
    public long getConnectionTimeout() {
        return DataSourceType.READ_WRITE.getConnectionTimeout();
    }

    @Override
    public long getMaxLifetime() {
        return DataSourceType.READ_WRITE.getMaxLifetime();
    }

    @Bean
    @MasterDataSource
    public DataSource masterDataSource(@MasterProperties final DatabaseConnectionProperties properties) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());

        return definePoolDataSourceConnection(dataSource);
    }

}
