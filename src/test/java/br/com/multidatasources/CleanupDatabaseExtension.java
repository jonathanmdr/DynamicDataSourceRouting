package br.com.multidatasources;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

@ActiveProfiles("integration-test")
public class CleanupDatabaseExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(final ExtensionContext context) {
        final var applicationContext = SpringExtension.getApplicationContext(context);
        final var repositoryBeans = applicationContext.getBeansWithAnnotation(Repository.class);
        cleanupDatabase(repositoryBeans);
    }

    private static void cleanupDatabase(final Map<String, Object> repositoryBeans) {
        repositoryBeans.values().forEach(CleanupDatabaseExtension::deleteAll);
    }

    private static void deleteAll(final Object repository) {
        switch (repository) {
            case JpaRepository<?, ?> jpaRepository -> jpaRepository.deleteAllInBatch();
            case CrudRepository<?, ?> crudRepository -> crudRepository.deleteAll();
            default -> throw new IllegalArgumentException("Unsupported repository type: " + repository.getClass());
        }
    }

}
