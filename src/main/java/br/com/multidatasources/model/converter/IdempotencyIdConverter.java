package br.com.multidatasources.model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.UUID;

@Converter
public class IdempotencyIdConverter implements AttributeConverter<UUID, String> {

    @Override
    public String convertToDatabaseColumn(final UUID uuid) {
        return uuid.toString();
    }

    @Override
    public UUID convertToEntityAttribute(final String value) {
        return UUID.fromString(value);
    }

}
