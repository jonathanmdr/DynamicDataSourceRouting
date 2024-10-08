package br.com.multidatasources.service.v1;

import br.com.multidatasources.model.Billionaire;
import br.com.multidatasources.model.factory.BillionaireBuilder;
import br.com.multidatasources.repository.BillionaireRepository;
import br.com.multidatasources.service.v1.idempotency.IdempotencyGenerator;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class BillionaireServiceOldTest {

    @Mock
    private BillionaireRepository billionaireRepository;

    @Mock
    private IdempotencyGenerator idempotencyGenerator;

    @InjectMocks
    private BillionaireService subject;

    @Test
    void givenAValidBillionaireId_whenFindBillionaireById_thenReturnASameBillionaireInformed() {
        final var expected = BillionaireBuilder.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .career("Software Developer")
            .build();

        when(billionaireRepository.findById(anyLong())).thenReturn(Optional.of(expected));

        final var actual = subject.findById(1L);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void givenAInvalidBillionaireId_whenFindBillionaireById_thenThrowEntityNotFoundException() {
        when(billionaireRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> subject.findById(2L))
            .isExactlyInstanceOf(EntityNotFoundException.class)
            .hasMessage("Register with id 2 not found");
    }

    @Test
    void givenATwoBillionaires_whenFindAll_thenReturnListWithTwoRegistries() {
        final var billionaireOne = BillionaireBuilder.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .career("Software Developer")
            .build();

        final var billionaireTwo = BillionaireBuilder.builder()
            .id(2L)
            .firstName("Richard")
            .lastName("Doe")
            .career("Software Developer")
            .build();

        List<Billionaire> expected = Arrays.asList(billionaireOne, billionaireTwo);

        when(billionaireRepository.findAll()).thenReturn(expected);

        List<Billionaire> actual = subject.findAll();

        assertThat(actual)
            .hasSize(2)
            .containsExactlyElementsOf(expected);
    }

    @Test
    void givenEmptyDataBillionaires_whenFindAll_thenReturnListWithZeroRegistries() {
        when(billionaireRepository.findAll()).thenReturn(Collections.emptyList());

        final var actual = subject.findAll();

        assertThat(actual).isEmpty();
    }

    @Test
    void givenANewBillionaire_whenSave_thenReturnASameBillionaireSaved() {
        Billionaire expected = BillionaireBuilder.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .career("Software Developer")
            .build();

        final var idempotencyId = UUID.randomUUID();

        when(idempotencyGenerator.apply(any(Billionaire.class))).thenReturn(idempotencyId);
        when(billionaireRepository.existsBillionaireByIdempotencyId(idempotencyId)).thenReturn(Boolean.FALSE);
        when(billionaireRepository.save(any(Billionaire.class))).thenReturn(expected);

        Billionaire actual = subject.save(expected);

        assertThat(actual).isEqualTo(expected);
        verify(billionaireRepository).save(any(Billionaire.class));
    }

    @Test
    void givenAnExistentBillionaire_whenSave_thenThrowsEntityExistsException() {
        Billionaire expected = BillionaireBuilder.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .career("Software Developer")
            .build();

        final var idempotencyId = UUID.randomUUID();

        when(idempotencyGenerator.apply(any(Billionaire.class))).thenReturn(idempotencyId);
        when(billionaireRepository.existsBillionaireByIdempotencyId(idempotencyId)).thenReturn(Boolean.TRUE);

        assertThatThrownBy(() -> subject.save(expected))
            .isExactlyInstanceOf(EntityExistsException.class)
            .hasMessage("Register has exists with idempotency ID: %s".formatted(idempotencyId));

        verify(billionaireRepository, never()).save(any(Billionaire.class));
    }

    @Test
    void givenAValidBillionaire_whenDelete_thenRepositoryDeleteMethodHasCalledOneTimes() {
        final var billionaire = BillionaireBuilder.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .career("Software Developer")
            .build();

        doNothing()
            .when(billionaireRepository).delete(any(Billionaire.class));

        subject.delete(billionaire);

        verify(billionaireRepository).delete(any(Billionaire.class));
    }

}
