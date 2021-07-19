package br.com.multidatasources.multidatasources.config.routing;

import static br.com.multidatasources.multidatasources.config.datasource.DataSourceType.READ_ONLY;
import static br.com.multidatasources.multidatasources.config.datasource.DataSourceType.READ_WRITE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class TransactionRoutingDataSource extends AbstractRoutingDataSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionRoutingDataSource.class);

    @Override
    protected Object determineCurrentLookupKey() {
        if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
            LOGGER.info("Read DB selected");
            return READ_ONLY;
        }

        LOGGER.info("Write DB selected");
        return READ_WRITE;
    }

}
