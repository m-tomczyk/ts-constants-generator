package dev.mtomczyk.typescript.generator.classes.custom.mappers;

import dev.mtomczyk.typescript.generator.TsType;
import dev.mtomczyk.typescript.generator.classes.custom.SimpleCurrency;
import dev.mtomczyk.typescript.generator.Mapper;

public class SimpleCurrencyMapper implements Mapper<SimpleCurrency> {
    @Override
    public Class<SimpleCurrency> getMappedType() {
        return SimpleCurrency.class;
    }

    @Override
    public TsType getTsType() {
        return TsType.NUMBER;
    }

    @Override
    public String getValue(SimpleCurrency object) {
        return String.valueOf(object.getWhole()) + "." + String.valueOf(object.getDecimal());
    }
}
