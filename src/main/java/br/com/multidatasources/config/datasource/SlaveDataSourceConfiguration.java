package br.com.multidatasources.config.datasource;

import br.com.multidatasources.config.properties.datasource.DatabaseConnectionProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

import static br.com.multidatasources.config.properties.datasource.DataSourceConnectionPropertiesConfiguration.SLAVE_PROPERTIES_QUALIFIER;

@Configuration
public class SlaveDataSourceConfiguration implements DataSourceConfiguration {

    public static final String SLAVE_DATA_SOURCE_QUALIFIER = "slaveDataSource";

    @Override
    public String getPoolName() {
        return DataSourceType.READ_ONLY.getPoolName();
    }

    @Override
    public int getMinimumIdle() {
        return DataSourceType.READ_ONLY.getMinimumIdle();
    }

    @Override
    public int getMaximumPoolSize() {
        return DataSourceType.READ_ONLY.getMaximumPoolSize();
    }

    @Override
    public long getConnectionTimeout() {
        return DataSourceType.READ_ONLY.getConnectionTimeout();
    }

    @Override
    public long getMaxLifetime() {
        return DataSourceType.READ_ONLY.getMaxLifetime();
    }

    @Bean(name = SLAVE_DATA_SOURCE_QUALIFIER)
    public DataSource slaveDataSource(@Qualifier(SLAVE_PROPERTIES_QUALIFIER) final DatabaseConnectionProperties properties) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());

        return definePoolDataSourceConnection(dataSource);
    }

}
