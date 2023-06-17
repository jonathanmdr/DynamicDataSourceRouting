package br.com.multidatasources.service.v1.idempotency.impl;

import br.com.multidatasources.model.IdempotentEntity;
import br.com.multidatasources.service.v1.idempotency.IdempotencyGenerator;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.UUID;

@Component
public class UUIDIdempotencyGenerator implements IdempotencyGenerator {

    @Override
    public UUID generate(final IdempotentEntity data) {
        final var byteBuffer = ByteBuffer.allocate(Long.BYTES);
        final var bytes = byteBuffer.putLong(data.hashCode()).array();
        return UUID.nameUUIDFromBytes(bytes);
    }

}
