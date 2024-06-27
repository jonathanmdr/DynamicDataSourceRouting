package br.com.multidatasources.service.v1;

import br.com.multidatasources.DefaultBillionaire;
import br.com.multidatasources.DefaultBillionaireParameterResolverExtension;
import br.com.multidatasources.IntegrationTest;
import br.com.multidatasources.model.Billionaire;
import br.com.multidatasources.repository.BillionaireRepository;
import br.com.multidatasources.service.v1.idempotency.IdempotencyGenerator;
import br.com.multidatasources.service.v1.idempotency.impl.UUIDIdempotencyGenerator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

@IntegrationTest
@ExtendWith(DefaultBillionaireParameterResolverExtension.class)
class BillionaireServiceIntegrationTest {

    @SpyBean
    private IdempotencyGenerator idempotencyGenerator;

    @Autowired
    @SpyBean
    private BillionaireRepository billionaireRepository;

    @Autowired
    private BillionaireService billionaireService;

    @Test
    void givenAValidBillionaireId_whenFindBillionaireById_thenReturnASameBillionaireInformed(@DefaultBillionaire final Billionaire billionaire) {
        final var localIdempotencyGenerator = new UUIDIdempotencyGenerator();
        billionaire.generateIdempotencyId(localIdempotencyGenerator);
        final var persistedBillionaire = this.billionaireRepository.saveAndFlush(billionaire);

        final var actual = this.billionaireService.findById(persistedBillionaire.getId());

        assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(persistedBillionaire);

        verify(this.billionaireRepository).findById(persistedBillionaire.getId());
    }

    @Test
    void givenAInvalidBillionaireId_whenFindBillionaireById_thenThrowEntityNotFoundException() {
        final var invalidBillionaireId = 0L;

        assertThatThrownBy(() -> this.billionaireService.findById(invalidBillionaireId))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Register with id 0 not found");

        verify(this.billionaireRepository).findById(invalidBillionaireId);
    }

    @Test
    void givenATwoBillionaires_whenFindAll_thenReturnListWithTwoRegistries(
        @DefaultBillionaire final Billionaire billionaireOne,
        @DefaultBillionaire(firstName = "Jake") final Billionaire billionaireTwo
    ) {
        final var localIdempotencyGenerator = new UUIDIdempotencyGenerator();
        billionaireOne.generateIdempotencyId(localIdempotencyGenerator);
        billionaireTwo.generateIdempotencyId(localIdempotencyGenerator);
        this.billionaireRepository.saveAllAndFlush(List.of(billionaireOne, billionaireTwo));

        final var actual = this.billionaireService.findAll();

        assertThat(actual)
            .hasSize(2)
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyInAnyOrder(billionaireOne, billionaireTwo);

        verify(this.billionaireRepository).findAll();
    }

    @Test
    void givenEmptyDataBillionaires_whenFindAll_thenReturnListWithZeroRegistries() {
        final var actual = this.billionaireService.findAll();
        assertThat(actual).isEmpty();
        verify(this.billionaireRepository).findAll();
    }

    @Test
    void givenANewBillionaire_whenSave_thenReturnASameBillionaireSaved(@DefaultBillionaire final Billionaire billionaire) {
        final var localIdempotencyGenerator = new UUIDIdempotencyGenerator();
        billionaire.generateIdempotencyId(localIdempotencyGenerator);

        final var actual = this.billionaireService.save(billionaire);

        assertThat(actual)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(billionaire);

        verify(this.idempotencyGenerator).generate(billionaire);
        verify(this.billionaireRepository).existsBillionaireByIdempotencyId(billionaire.getIdempotencyId());
        verify(this.billionaireRepository).save(billionaire);
    }

}
