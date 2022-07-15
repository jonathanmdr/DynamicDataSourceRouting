package br.com.multidatasources.config.routing;

import br.com.multidatasources.config.datasource.DataSourceType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static br.com.multidatasources.config.datasource.MasterDataSourceConfiguration.MASTER_DATA_SOURCE_QUALIFIER;
import static br.com.multidatasources.config.datasource.SlaveDataSourceConfiguration.SLAVE_DATA_SOURCE_QUALIFIER;

@Configuration
public class DataSourceRoutingConfiguration {

    @Bean
    @Primary
    public TransactionRoutingDataSource routingDataSource(
            @Qualifier(MASTER_DATA_SOURCE_QUALIFIER) final DataSource masterDataSource,
            @Qualifier(SLAVE_DATA_SOURCE_QUALIFIER) final DataSource slaveDataSource
    ) {
        final TransactionRoutingDataSource routingDataSource = new TransactionRoutingDataSource();

        final Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(DataSourceType.READ_WRITE, masterDataSource);
        dataSourceMap.put(DataSourceType.READ_ONLY, slaveDataSource);

        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);

        return routingDataSource;
    }

}
