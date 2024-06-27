package br.com.multidatasources.repository;

import br.com.multidatasources.DatabaseRepositoryIntegrationTest;
import br.com.multidatasources.model.factory.BillionaireBuilder;
import br.com.multidatasources.service.v1.idempotency.impl.UUIDIdempotencyGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DatabaseRepositoryIntegrationTest
class BillionaireRepositoryIntegrationTest {

    @Autowired
    private BillionaireRepository billionaireRepository;

    @Test
    void givenExistentIdempotencyId_whenExistsBillionaireByIdempotencyId_thenReturnTrue() {
        final var idempotencyGenerator = new UUIDIdempotencyGenerator();
        final var billionaire = BillionaireBuilder.builder()
            .firstName("John")
            .lastName("Doe")
            .career("SWE")
            .build();

        billionaire.generateIdempotencyId(idempotencyGenerator);
        this.billionaireRepository.save(billionaire);

        final var actual = this.billionaireRepository.existsBillionaireByIdempotencyId(billionaire.getIdempotencyId());

        assertThat(actual).isTrue();
    }

    @Test
    void givenANonExistentIdempotencyId_whenExistsBillionaireByIdempotencyId_thenReturnFalse() {
        final var actual = this.billionaireRepository.existsBillionaireByIdempotencyId(UUID.randomUUID());
        assertThat(actual).isFalse();
    }

    @Test
    void validateDeleteAllByCallbackExtension() {
        // Should be empty table on database
        assertThat(this.billionaireRepository.count()).isZero();

        final var idempotencyGenerator = new UUIDIdempotencyGenerator();

        // Generate 10 billionaires with random data
        final var billionaires = IntStream.range(1, 11)
            .mapToObj(index -> {
                final var billionaire = BillionaireBuilder.builder()
                    .firstName(RandomStringUtils.randomAlphabetic(index))
                    .lastName(RandomStringUtils.randomAlphabetic(index))
                    .career(RandomStringUtils.randomAlphabetic(index))
                    .build();
                billionaire.generateIdempotencyId(idempotencyGenerator);
                return billionaire;
            }).toList();

        // Save all billionaires
        this.billionaireRepository.saveAllAndFlush(billionaires);

        // Should have 10 billionaires on database
        assertThat(this.billionaireRepository.count()).isEqualTo(10);
    }

}
