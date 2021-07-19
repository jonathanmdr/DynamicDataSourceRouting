package br.com.multidatasources.multidatasources.config.datasource;

import br.com.multidatasources.multidatasources.config.properties.DatabaseConnectionProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class MasterDataSourceConfiguration extends AbstractDataSourceConfiguration {

    @Override
    public String getPoolName() {
        return DataSourceType.READ_WRITE.getPoolName();
    }

    @Override
    public int getMaximumPoolSize() {
        return Runtime.getRuntime().availableProcessors() * 4;
    }

    @Bean
    public DataSource masterDataSource(@Qualifier("masterProperties") DatabaseConnectionProperties properties) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());

        return super.definePoolDataSourceConnection(dataSource);
    }

}
