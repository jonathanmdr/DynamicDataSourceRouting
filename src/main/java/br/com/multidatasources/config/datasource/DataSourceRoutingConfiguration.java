package br.com.multidatasources.config.datasource;

import br.com.multidatasources.config.datasource.master.MasterDataSource;
import br.com.multidatasources.config.datasource.replica.ReplicaDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceRoutingConfiguration {

    @Bean
    @Primary
    public TransactionRoutingDataSource routingDataSource(
        @MasterDataSource final DataSource masterDataSource,
        @ReplicaDataSource final DataSource replicaDataSource
    ) {
        final var routingDataSource = new TransactionRoutingDataSource();

        final Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(DataSourceType.READ_WRITE, masterDataSource);
        dataSourceMap.put(DataSourceType.READ_ONLY, replicaDataSource);

        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);

        return routingDataSource;
    }

}
