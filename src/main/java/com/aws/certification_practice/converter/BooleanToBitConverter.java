package com.aws.certification_practice.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BooleanToBitConverter implements AttributeConverter<Boolean, Byte> {
    @Override
    public Byte convertToDatabaseColumn(Boolean attribute) {
        return (attribute != null && attribute) ? (byte) 1 : (byte) 0;
    }

    @Override
    public Boolean convertToEntityAttribute(Byte dbData) {
        return dbData != null && dbData == 1;
    }
}
