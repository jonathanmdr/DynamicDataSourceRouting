package br.com.multidatasources.service;

import br.com.multidatasources.model.Billionaire;
import br.com.multidatasources.repository.BillionaireRepository;
import br.com.multidatasources.service.idempotency.IdempotencyGenerator;
import br.com.multidatasources.service.idempotency.impl.UUIDIdempotencyGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class BillionaireServiceNewTest {

    private BillionaireService subject;

    @Test
    void givenAValidBillionaireId_whenFindBillionaireById_thenReturnASameBillionaireInformed() {
          given()
               .billionaire()
                   .id(1L).ok()
                   .firstName("John")
                   .lastName("Doe")
                   .career("Software Developer")
               .end()
          .when(subject::findById, 1L)
          .then()
               .asserter()
                   .idIsEqualTo(1L)
                   .firstNameIsEqualTo("John")
                   .lastNameIsEqualTo("Doe")
                   .careerIsEqualTo("Software Developer");
    }

    @Test
    void givenAInvalidBillionaireId_whenFindBillionaireById_thenThrowEntityNotFoundException() {
        given()
            .billionaire()
                .id(2L).notFound()
            .end()
        .when(subject::findById, 2L)
        .then()
            .exceptionAsserter()
                .isInstanceOf(EntityNotFoundException.class)
                .messageIsEqualTo("Register with id 2 not found");
    }

    @Test
    void givenATwoBillionaires_whenFindAll_thenReturnListWithTwoRegistries() {
        given()
            .billionaire()
                .id(1L).ok()
            .save()
            .billionaire()
                .id(2L).ok()
            .save()
        .when(subject::findAll)
        .then()
            .listAsserter()
                .sizeIsEqualTo(2);
    }

    @Test
    void givenEmptyDataBillionaires_whenFindAll_thenReturnListWithZeroRegistries() {
        given()
            .emptyData()
        .when(subject::findAll)
        .then()
            .listAsserter()
                .sizeIsEqualTo(0);
    }

    @Test
    void givenANewBillionaire_whenSave_thenReturnASameBillionaireSaved() {
        given()
            .billionaire()
                .firstName("Richard")
                .lastName("Brand")
                .career("Software Engineering")
            .end()
        .when(subject::save)
        .then()
            .asserter()
                .firstNameIsEqualTo("Richard")
                .lastNameIsEqualTo("Brand")
                .careerIsEqualTo("Software Engineering");
    }

    @Test
    void givenAnExistentBillionaire_whenSave_thenThrowsEntityExistsException() {
        given()
            .billionaire()
                .id(1L).hasExistsOnDatabase()
                .firstName("Richard")
                .lastName("Brand")
                .career("Software Engineering")
            .end()
        .when(subject::save)
        .then()
            .exceptionAsserter()
                .isInstanceOf(EntityExistsException.class)
                .messageIsEqualTo("Register has exists with idempotency ID: c2ea0bd8-516b-34d1-bc47-7645b8c2e524");
    }

    @Test
    void givenAValidBillionaire_whenDelete_thenRepositoryDeleteMethodHasCalledOneTimes() {
        given()
            .billionaire()
                .id(1L).ok()
            .end()
        .when(subject::delete)
        .then()
            .asserter()
                .verifyRepositoryDeleteMethodHasCalled();
    }

    Dsl given() {
        return new Dsl();
    }

    class Dsl {
        private final BillionaireRepository billionaireRepository;
        private final IdempotencyGenerator idempotencyGenerator;
        private Billionaire expected;
        private Billionaire actual;
        private List<Billionaire> expectedList;
        private List<Billionaire> actualList;
        private Exception actualException;

        Dsl() {
            this.billionaireRepository = mock(BillionaireRepository.class);
            this.idempotencyGenerator = new UUIDIdempotencyGenerator();

            subject = new BillionaireService(billionaireRepository, idempotencyGenerator);
        }

        BillionaireDsl billionaire() {
            return new BillionaireDsl();
        }

        Dsl emptyData() {
            actualList = new ArrayList<>();

            Mockito.when(billionaireRepository.findAll()).thenReturn(Collections.emptyList());

            return this;
        }

        Dsl when(Function<Long, Billionaire> function, long billionaireId) {
            try {
                actual = function.apply(billionaireId);
            } catch (Exception exception) {
                actualException = exception;
            }
            return this;
        }

        Dsl when(Supplier<List<Billionaire>> supplier) {
            actualList = supplier.get();
            return this;
        }

        Dsl when(Function<Billionaire, Billionaire> function) {
            try {
                actual = function.apply(expected);
            } catch (Exception exception) {
                actualException = exception;
            }
            return this;
        }

        Dsl when(Consumer<Billionaire> consumer) {
            consumer.accept(expected);
            return this;
        }

        DslAsserter then() {
            return new DslAsserter();
        }

        class BillionaireDsl {

            BillionaireDsl() {
                expected = new Billionaire();

                Mockito.when(billionaireRepository.findAll()).thenReturn(expectedList);
                Mockito.when(billionaireRepository.save(any(Billionaire.class))).thenReturn(expected);
                doNothing().when(billionaireRepository).delete(expected);
                Mockito.when(billionaireRepository.existsBillionaireByIdempotencyId(any(UUID.class))).thenReturn(Boolean.FALSE);
            }

            BillionaireId id(Long id) {
                return new BillionaireId(id);
            }

            BillionaireDsl firstName(String firstName) {
                expected.setFirstName(firstName);
                return this;
            }

            BillionaireDsl lastName(String lastName) {
                expected.setLastName(lastName);
                return this;
            }

            BillionaireDsl career(String career) {
                expected.setCareer(career);
                return this;
            }

            Dsl save() {
                if (Objects.isNull(expectedList)) {
                    expectedList = new ArrayList<>();
                }

                expectedList.add(expected);
                return Dsl.this;
            }

            Dsl end() {
                return Dsl.this;
            }

            class BillionaireId {

                BillionaireId(Long id) {
                    expected.setId(id);
                }

                BillionaireId notFound() {
                    Mockito.when(billionaireRepository.findById(expected.getId())).thenReturn(Optional.empty());
                    return this;
                }

                BillionaireDsl ok() {
                    Mockito.when(billionaireRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
                    return BillionaireDsl.this;
                }

                BillionaireDsl hasExistsOnDatabase() {
                    Mockito.when(billionaireRepository.existsBillionaireByIdempotencyId(any(UUID.class))).thenReturn(Boolean.TRUE);
                    return BillionaireDsl.this;
                }

                Dsl end() {
                    return Dsl.this;
                }

            }

        }

        class DslAsserter {

            BillionaireDslAsserter asserter() {
                return new BillionaireDslAsserter();
            }

            ExceptionDslAsserter exceptionAsserter() {
                return new ExceptionDslAsserter();
            }

            ListDslAsserter listAsserter() {
                return new ListDslAsserter();
            }

            class BillionaireDslAsserter {

                BillionaireDslAsserter idIsEqualTo(final Long expected) {
                    assertThat(actual.getId()).isEqualTo(expected);
                    return this;
                }

                BillionaireDslAsserter firstNameIsEqualTo(final String expected) {
                    assertThat(actual.getFirstName()).isEqualTo(expected);
                    return this;
                }

                BillionaireDslAsserter lastNameIsEqualTo(final String expected) {
                    assertThat(actual.getLastName()).isEqualTo(expected);
                    return this;
                }

                void careerIsEqualTo(final String expected) {
                    assertThat(actual.getCareer()).isEqualTo(expected);
                }

                void verifyRepositoryDeleteMethodHasCalled() {
                    verify(billionaireRepository, times(1)).delete(expected);
                }

            }

            class ExceptionDslAsserter {

                ExceptionDslAsserter isInstanceOf(final Class<? extends Exception> expected) {
                    assertThat(actualException).isInstanceOf(expected);
                    return this;
                }

                void messageIsEqualTo(final String expected) {
                    assertThat(actualException.getMessage()).isEqualTo(expected);
                }

            }

            class ListDslAsserter {

                void sizeIsEqualTo(final int expected) {
                    assertThat(actualList).hasSize(expected);
                }

            }

        }

    }

}
