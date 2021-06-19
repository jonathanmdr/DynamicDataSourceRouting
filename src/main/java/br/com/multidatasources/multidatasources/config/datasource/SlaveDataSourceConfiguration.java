package br.com.multidatasources.multidatasources.config.datasource;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import br.com.multidatasources.multidatasources.config.properties.DatabaseConnectionProperties;

@Configuration
public class SlaveDataSourceConfiguration extends AbstractDataSourceConfiguration {

    @Override
    public String getPoolName() {
        return DataSourceType.READ_ONLY.getPoolName();
    }

    @Override
    public int getMaximunPoolSize() {
        return Runtime.getRuntime().availableProcessors() * 4;
    }

    @Override
    public boolean isAutoCommitEnabled() {
        return false;
    }

    @Bean
    public DataSource slaveDataSource(@Qualifier("slaveProperties") DatabaseConnectionProperties properties) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        return definePoolDataSourceConnection(dataSource);
    }

}
