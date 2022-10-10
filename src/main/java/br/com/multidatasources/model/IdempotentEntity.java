package br.com.multidatasources.model;

import br.com.multidatasources.model.converter.IdempotencyIdConverter;
import br.com.multidatasources.service.idempotency.IdempotencyGenerator;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@MappedSuperclass
public abstract class IdempotentEntity<T> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private T id;

    @Column(name = "idempotency_id")
    @Convert(converter = IdempotencyIdConverter.class)
    private UUID idempotencyId;

    public abstract void generateIdempotencyId(final IdempotencyGenerator generator);

    public T getId() {
        return id;
    }

    public void setId(final T id) {
        this.id = id;
    }

    public UUID getIdempotencyId() {
        return idempotencyId;
    }

    public void setIdempotencyId(final UUID idempotencyId) {
        this.idempotencyId = idempotencyId;
    }

}
