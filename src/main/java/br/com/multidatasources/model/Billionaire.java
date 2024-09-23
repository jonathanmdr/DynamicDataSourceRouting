package br.com.multidatasources.model;

import br.com.multidatasources.service.v1.idempotency.IdempotencyGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.Objects;

@Entity(name = "Billionaire")
@Table(
    name = "billionaires",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_billionaires_idempotency_id",
            columnNames = {
                "idempotency_id"
            }
        )
    }
)
public class Billionaire extends IdempotentEntity {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    private String career;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(final String career) {
        this.career = career;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Billionaire that = (Billionaire) o;
        return Objects.equals(getFirstName(), that.getFirstName()) && Objects.equals(getLastName(), that.getLastName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName());
    }

    @Override
    public void generateIdempotencyId(final IdempotencyGenerator generator) {
        final var uuid = generator.apply(this);
        this.setIdempotencyId(uuid);
    }

}
