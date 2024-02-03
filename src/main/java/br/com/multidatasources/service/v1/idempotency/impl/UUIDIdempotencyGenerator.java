package br.com.multidatasources.service.v1.idempotency.impl;

import br.com.multidatasources.model.IdempotentEntity;
import br.com.multidatasources.service.v1.idempotency.Algorithm;
import br.com.multidatasources.service.v1.idempotency.IdempotencyGenerator;
import br.com.multidatasources.util.JsonUtils;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Component
public class UUIDIdempotencyGenerator implements IdempotencyGenerator {

    @Override
    public UUID generate(final IdempotentEntity data) {
        try {
            final var messageDigest = MessageDigest.getInstance(Algorithm.SHA_256.value());
            final var jsonDataBytes = JsonUtils.toJson(data).getBytes(StandardCharsets.UTF_8);
            return UUID.nameUUIDFromBytes(messageDigest.digest(jsonDataBytes));
        } catch (final NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Algorithm not found", ex);
        }
    }

}
