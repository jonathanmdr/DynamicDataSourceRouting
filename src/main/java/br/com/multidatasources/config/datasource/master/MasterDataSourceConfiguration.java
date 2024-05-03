package br.com.multidatasources.config.datasource.master;

import br.com.multidatasources.config.datasource.DataSourceConfiguration;
import br.com.multidatasources.config.datasource.DataSourceType;
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
    public DataSourceType dataSourceType() {
        return READ_WRITE;
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
