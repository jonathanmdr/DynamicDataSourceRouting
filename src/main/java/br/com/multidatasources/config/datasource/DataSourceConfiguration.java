package br.com.multidatasources.config.datasource;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public interface DataSourceConfiguration {

    String poolName();

    int minimumIdle();

    int maximumPoolSize();

    long connectionTimeout();

    long idleTimeout();

    long maxLifetime();

    default HikariDataSource definePoolDataSourceConnection(final DataSource dataSource) {
        return new HikariDataSource(hikariConfig(dataSource));
    }

    private HikariConfig hikariConfig(final DataSource dataSource) {
        final HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setPoolName(poolName());
        hikariConfig.setMaximumPoolSize(maximumPoolSize());
        hikariConfig.setMinimumIdle(minimumIdle());
        hikariConfig.setConnectionTimeout(connectionTimeout());
        hikariConfig.setMaxLifetime(maxLifetime());
        hikariConfig.setIdleTimeout(idleTimeout());
        hikariConfig.setDataSource(dataSource);
        hikariConfig.setAutoCommit(false);

        return hikariConfig;
    }

}
