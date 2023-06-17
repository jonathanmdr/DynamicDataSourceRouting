package br.com.multidatasources.config.routing;

import io.opentelemetry.api.trace.Span;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static br.com.multidatasources.config.datasource.DataSourceType.READ_ONLY;
import static br.com.multidatasources.config.datasource.DataSourceType.READ_WRITE;

public class TransactionRoutingDataSource extends AbstractRoutingDataSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionRoutingDataSource.class);

    @Override
    protected Object determineCurrentLookupKey() {
        if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {

            LOGGER.info("Routed to: {}", READ_ONLY);
            enrichSpan(READ_ONLY.name(), READ_ONLY.poolName());

            return READ_ONLY;
        }

        LOGGER.info("Routed to: {}", READ_WRITE);
        enrichSpan(READ_WRITE.name(), READ_WRITE.poolName());

        return READ_WRITE;
    }

    private void enrichSpan(final String dbType, final String pollName) {
        final var currentSpan = Span.current();
        currentSpan.setAttribute("db.type", dbType);
        currentSpan.setAttribute("db.poll", pollName);
    }

}
