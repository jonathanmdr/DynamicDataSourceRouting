package br.com.multidatasources;

import br.com.multidatasources.config.datasource.DataSourceRoutingConfiguration;
import br.com.multidatasources.config.datasource.master.MasterDataSourceConfiguration;
import br.com.multidatasources.config.datasource.replica.ReplicaDataSourceConfiguration;
import br.com.multidatasources.config.properties.datasource.DataSourceConnectionPropertiesConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("integration-test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(CleanupDatabaseExtension.class)
@Import(
    value = {
        MasterDataSourceConfiguration.class,
        ReplicaDataSourceConfiguration.class,
        DataSourceRoutingConfiguration.class,
        DataSourceConnectionPropertiesConfiguration.class
    }
)
public @interface DatabaseRepositoryIntegrationTest {

}
