package br.com.multidatasources.service;

import br.com.multidatasources.model.Billionaire;
import br.com.multidatasources.model.factory.BillionaireBuilder;
import br.com.multidatasources.repository.BillionaireRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class BillionaireServiceOldTest {

    @Mock
    private BillionaireRepository billionaireRepository;

    @InjectMocks
    private BillionaireService subject;

    @Test
    void givenAValidBillionaireId_whenFindBillionaireById_thenReturnASameBillionaireInformed() {
        Billionaire expected = BillionaireBuilder.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .career("Software Developer")
            .build();

        when(billionaireRepository.findById(anyLong())).thenReturn(Optional.of(expected));

        Billionaire actual = subject.findById(1L);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void givenAInvalidBillionaireId_whenFindBillionaireById_thenThrowEntityNotFoundException() {
        when(billionaireRepository.findById(anyLong())).thenThrow(new EntityNotFoundException());

        Assertions.assertThrows(
            EntityNotFoundException.class,
            () -> subject.findById(2L),
            "Register with id 2 not found"
        );
    }

    @Test
    void givenATwoBillionaires_whenFindAll_thenReturnListWithTwoRegistries() {
        Billionaire billionaireOne = BillionaireBuilder.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .career("Software Developer")
            .build();

        Billionaire billionaireTwo = BillionaireBuilder.builder()
            .id(2L)
            .firstName("Richard")
            .lastName("Roe")
            .career("Software Developer")
            .build();

        List<Billionaire> expected = Arrays.asList(billionaireOne, billionaireTwo);

        when(billionaireRepository.findAll()).thenReturn(expected);

        List<Billionaire> actual = subject.findAll();

        assertThat(actual).hasSize(2);
        assertThat(actual).usingRecursiveComparison().isEqualTo(actual);
    }

    @Test
    void givenEmptyDataBillionaires_whenFindAll_thenReturnListWithZeroRegistries() {
        when(billionaireRepository.findAll()).thenReturn(Collections.emptyList());

        List<Billionaire> actual = subject.findAll();

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

        when(billionaireRepository.save(any(Billionaire.class))).thenReturn(expected);

        Billionaire actual = subject.save(expected);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void givenAValidBillionaire_whenDelete_thenRepositoryDeleteMethodHasCalledOneTimes() {
        Billionaire billionaire = BillionaireBuilder.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .career("Software Developer")
            .build();

        doNothing().when(billionaireRepository).delete(any(Billionaire.class));

        subject.delete(billionaire);

        verify(billionaireRepository, times(1)).delete(any(Billionaire.class));
    }

}
