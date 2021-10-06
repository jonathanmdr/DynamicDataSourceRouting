package br.com.multidatasources.multidatasources.service;

import br.com.multidatasources.multidatasources.model.Billionaire;
import br.com.multidatasources.multidatasources.repository.BillionaireRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class BillionaireServiceTest {

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
        private final BillionaireRepository billionaireRepository = mock(BillionaireRepository.class);
        private Billionaire expected;
        private Billionaire actual;
        private List<Billionaire> expectedList;
        private List<Billionaire> actualList;
        private Exception actualException;

        Dsl() {
            subject = new BillionaireService(billionaireRepository);
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
            } catch(Exception exception) {
                actualException = exception;
            }
            return this;
        }

        Dsl when(Supplier<List<Billionaire>> supplier) {
            actualList = supplier.get();
            return this;
        }

        Dsl when(Function<Billionaire, Billionaire> function) {
            actual = function.apply(expected);
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

            ListAsserter listAsserter() {
                return new ListAsserter();
            }

            class BillionaireDslAsserter {

                BillionaireDslAsserter idIsEqualTo(Long expectedBillionaireId) {
                    assertThat(actual.getId()).isEqualTo(expectedBillionaireId);
                    return this;
                }

                BillionaireDslAsserter firstNameIsEqualTo(String expectedBillionaireFirstName) {
                    assertThat(actual.getFirstName()).isEqualTo(expectedBillionaireFirstName);
                    return this;
                }

                BillionaireDslAsserter lastNameIsEqualTo(String expectedBillionaireLastName) {
                    assertThat(actual.getLastName()).isEqualTo(expectedBillionaireLastName);
                    return this;
                }

                void careerIsEqualTo(String expectedBillionaireCareer) {
                    assertThat(actual.getCareer()).isEqualTo(expectedBillionaireCareer);
                }

                void verifyRepositoryDeleteMethodHasCalled() {
                    verify(billionaireRepository, times(1)).delete(expected);
                }

            }

            class ExceptionDslAsserter {

                ExceptionDslAsserter isInstanceOf(Class<? extends Exception> expectedException) {
                    assertThat(actualException).isInstanceOf(expectedException);
                    return this;
                }

                void messageIsEqualTo(String expectedMessage) {
                    assertThat(actualException.getMessage()).isEqualTo(expectedMessage);
                }

            }

            class ListAsserter {

                void sizeIsEqualTo(int expectedSize) {
                    assertThat(actualList).hasSize(expectedSize);
                }

            }

        }

    }

}