package br.com.multidatasources.multidatasources.service;

import br.com.multidatasources.multidatasources.model.Billionaire;
import br.com.multidatasources.multidatasources.repository.BillionaireRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;

class BillionaireServiceTest {

    private BillionaireService subject;

    @Test
    void givenAValidBillionaireId_whenFindBillionaireById_thenReturnASameBillionaireInformed() {
          given()
               .billionaire()
                   .id(1L)
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
    void findAll() {
    }

    @Test
    void save() {
    }

    @Test
    void delete() {
    }

    Dsl given() {
        return new Dsl();
    }

    class Dsl {
        private final BillionaireRepository billionaireRepository = mock(BillionaireRepository.class);
        private Billionaire expected;
        private Billionaire actual;

        Dsl() {
            subject = new BillionaireService(billionaireRepository);
        }

        BillionaireDsl billionaire() {
            return new BillionaireDsl();
        }

        Dsl when(Function<Long, Billionaire> function, long billionaireId) {
            actual = function.apply(billionaireId);
            return this;
        }

        DslAsserter then() {
            return new DslAsserter();
        }

        class BillionaireDsl {

            BillionaireDsl() {
                expected = new Billionaire();

                Mockito.when(billionaireRepository.findById(anyLong())).thenReturn(Optional.of(expected));
            }

            BillionaireDsl id(Long id) {
                expected.setId(id);
                return this;
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

            Dsl end() {
                return Dsl.this;
            }

        }

        class DslAsserter {

            BillionaireDslAsserter asserter() {
                return new BillionaireDslAsserter();
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

            }

        }

    }

}