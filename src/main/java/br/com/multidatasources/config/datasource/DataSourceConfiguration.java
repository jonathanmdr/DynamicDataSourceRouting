package br.com.multidatasources.config.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public interface DataSourceConfiguration {

    DataSourceType dataSourceType();

    default HikariDataSource definePoolDataSourceConnection(final DataSource dataSource) {
        return new HikariDataSource(hikariConfig(dataSource));
    }

    private HikariConfig hikariConfig(final DataSource dataSource) {
        final HikariConfig hikariConfig = new HikariConfig();
        final DataSourceType dataSourceType = dataSourceType();

        hikariConfig.setPoolName(dataSourceType.poolName());
        hikariConfig.setMaximumPoolSize(dataSourceType.maximumPoolSize());
        hikariConfig.setMinimumIdle(dataSourceType.minimumIdle());
        hikariConfig.setConnectionTimeout(dataSourceType.connectionTimeout());
        hikariConfig.setMaxLifetime(dataSourceType.maxLifetime());
        hikariConfig.setIdleTimeout(dataSourceType.idleTimeout());
        hikariConfig.setDataSource(dataSource);
        hikariConfig.setAutoCommit(false);

        return hikariConfig;
    }

}
