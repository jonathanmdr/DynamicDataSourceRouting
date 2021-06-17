package br.com.multidatasources.multidatasources.config.routing;

import static br.com.multidatasources.multidatasources.config.datasource.DataSourceType.READ_ONLY;
import static br.com.multidatasources.multidatasources.config.datasource.DataSourceType.READ_WRITE;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class TransactionRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return TransactionSynchronizationManager.isCurrentTransactionReadOnly() ? READ_ONLY : READ_WRITE;
    }

}
