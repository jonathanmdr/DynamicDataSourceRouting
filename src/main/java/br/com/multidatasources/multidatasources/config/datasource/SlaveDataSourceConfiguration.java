package br.com.multidatasources.multidatasources.config.datasource;

import br.com.multidatasources.multidatasources.config.properties.datasource.DatabaseConnectionProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

import static br.com.multidatasources.multidatasources.config.properties.datasource.DataSourceConnectionPropertiesConfiguration.SLAVE_PROPERTIES_QUALIFIER;

@Configuration
public class SlaveDataSourceConfiguration extends AbstractDataSourceConfiguration {

    public static final String SLAVE_DATA_SOURCE_QUALIFIER = "slaveDataSource";

    @Override
    public String getPoolName() {
        return DataSourceType.READ_ONLY.getPoolName();
    }

    @Override
    public int getMaximumPoolSize() {
        return Runtime.getRuntime().availableProcessors() * 4;
    }

    @Bean(name = SLAVE_DATA_SOURCE_QUALIFIER)
    public DataSource slaveDataSource(@Qualifier(SLAVE_PROPERTIES_QUALIFIER) DatabaseConnectionProperties properties) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());

        return super.definePoolDataSourceConnection(dataSource);
    }

}
