package br.com.multidatasources.service.v1.idempotency;

import br.com.multidatasources.model.IdempotentEntity;

import java.util.UUID;
import java.util.function.Function;

@FunctionalInterface
public interface IdempotencyGenerator extends Function<IdempotentEntity, UUID> {

}
