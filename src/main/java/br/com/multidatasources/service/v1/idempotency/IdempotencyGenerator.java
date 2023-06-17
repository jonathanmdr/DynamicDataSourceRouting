package br.com.multidatasources.service.v1.idempotency;

import br.com.multidatasources.model.IdempotentEntity;

import java.util.UUID;

@FunctionalInterface
public interface IdempotencyGenerator {

    UUID generate(final IdempotentEntity entity);

}
