package br.com.multidatasources.model;

import br.com.multidatasources.model.converter.IdempotencyIdConverter;
import br.com.multidatasources.service.v1.idempotency.IdempotencyGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.util.UUID;

@MappedSuperclass
public abstract class IdempotentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "idempotency_id", nullable = false, updatable = false)
    @Convert(converter = IdempotencyIdConverter.class)
    private UUID idempotencyId;

    public abstract void generateIdempotencyId(final IdempotencyGenerator generator);

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public UUID getIdempotencyId() {
        return idempotencyId;
    }

    public void setIdempotencyId(final UUID idempotencyId) {
        this.idempotencyId = idempotencyId;
    }

}
