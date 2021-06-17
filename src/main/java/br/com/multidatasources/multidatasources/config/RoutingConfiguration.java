package br.com.multidatasources.multidatasources.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import br.com.multidatasources.multidatasources.model.Billionaires;

@Configuration
@EnableTransactionManagement
public class RoutingConfiguration {

    private final Environment environment;

    public RoutingConfiguration(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("routingDataSource") DataSource routingDataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

        entityManagerFactoryBean.setPersistenceUnitName(getClass().getSimpleName());
        entityManagerFactoryBean.setPersistenceProvider(new HibernatePersistenceProvider());
        entityManagerFactoryBean.setDataSource(routingDataSource);
        entityManagerFactoryBean.setPackagesToScan(Billionaires.class.getPackageName());

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        HibernateJpaDialect jpaDialect = vendorAdapter.getJpaDialect();

        jpaDialect.setPrepareConnection(false);

        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        entityManagerFactoryBean.setJpaProperties(additionalProperties());

        return entityManagerFactoryBean;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    @Bean
    public TransactionTemplate transactionTemplate(EntityManagerFactory entityManagerFactory) {
        return new TransactionTemplate(transactionManager(entityManagerFactory));
    }

    @Bean
    public TransactionRoutingDataSource routingDataSource(
            @Qualifier("masterDataSource") DataSource masterDataSource,
            @Qualifier("slaveDataSource") DataSource slaveDataSource
    ) {
        TransactionRoutingDataSource routingDataSource = new TransactionRoutingDataSource();

        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(DataSourceType.READ_WRITE, masterDataSource);
        dataSourceMap.put(DataSourceType.READ_ONLY, slaveDataSource);

        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(masterDataSource());

        return routingDataSource;
    }

    @Bean
    public DataSource masterDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(environment.getProperty("master.datasource.url"));
        dataSource.setUsername(environment.getProperty("master.datasource.username"));
        dataSource.setPassword(environment.getProperty("master.datasource.password"));
        return connectionPoolDataSource(dataSource, determinePoolName(DataSourceType.READ_WRITE));
    }

    @Bean
    public DataSource slaveDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(environment.getProperty("slave.datasource.url"));
        dataSource.setUsername(environment.getProperty("slave.datasource.username"));
        dataSource.setPassword(environment.getProperty("slave.datasource.password"));
        return connectionPoolDataSource(dataSource, determinePoolName(DataSourceType.READ_ONLY));
    }

    private HikariDataSource connectionPoolDataSource(DataSource dataSource, String poolName) {
        return new HikariDataSource(hikariConfig(dataSource, poolName));
    }

    private HikariConfig hikariConfig(DataSource dataSource, String poolName) {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setPoolName(poolName);
        hikariConfig.setMaximumPoolSize(Runtime.getRuntime().availableProcessors() * 4);
        hikariConfig.setDataSource(dataSource);
        hikariConfig.setAutoCommit(false);

        return hikariConfig;
    }

    private Properties additionalProperties() {
        Properties properties = new Properties();

        properties.setProperty("hibernate.dialect", environment.getProperty("spring.jpa.database-platform"));
        properties.setProperty("hibernate.connection.provider_disables_autocommit", "true");

        return properties;
    }

    private String determinePoolName(DataSourceType dataSourceType) {
        return dataSourceType.getPoolName().concat("-").concat(dataSourceType.name());
    }

}
