package br.com.multidatasources.multidatasources.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import br.com.multidatasources.multidatasources.model.Billionaires;

@Configuration
@EnableTransactionManagement
public class RoutingConfiguration {
    
    private final Environment environment;
    
    public RoutingConfiguration(Environment environment) {
        this.environment = environment;
    }
    
    @Bean
    public JpaTransactionManager transactionManager(@Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory.getObject());
    }
    
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("routingDataSource") DataSource routingDataSource) {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(routingDataSource);
        bean.setPackagesToScan(Billionaires.class.getPackageName());
        bean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        bean.setJpaProperties(additionalProperties());
        return bean;
    }
    
    @Bean
    public DataSource dataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
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
        return dataSource;
    }

    @Bean
    public DataSource slaveDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(environment.getProperty("slave.datasource.url"));
        dataSource.setUsername(environment.getProperty("slave.datasource.username"));
        dataSource.setPassword(environment.getProperty("slave.datasource.password"));
        return dataSource;
    }
    
    private Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
           
        return properties;
    }

}
