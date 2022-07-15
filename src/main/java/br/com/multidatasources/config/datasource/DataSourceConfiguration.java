package br.com.multidatasources.config.datasource;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public interface DataSourceConfiguration {

    String getPoolName();

    int getMinimumIdle();

    int getMaximumPoolSize();

    long getConnectionTimeout();

    long getMaxLifetime();

    default HikariDataSource definePoolDataSourceConnection(final DataSource dataSource) {
        return new HikariDataSource(hikariConfig(dataSource));
    }

    private HikariConfig hikariConfig(final DataSource dataSource) {
        final HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setPoolName(getPoolName());
        hikariConfig.setMaximumPoolSize(getMaximumPoolSize());
        hikariConfig.setMinimumIdle(getMinimumIdle());
        hikariConfig.setConnectionTimeout(getConnectionTimeout());
        hikariConfig.setMaxLifetime(getMaxLifetime());
        hikariConfig.setDataSource(dataSource);
        hikariConfig.setAutoCommit(false);

        return hikariConfig;
    }

}
