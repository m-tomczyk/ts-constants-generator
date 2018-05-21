package org.mtomczyk.typescript.generator.classes.custom.mappers;

import org.mtomczyk.typescript.generator.TsType;
import org.mtomczyk.typescript.generator.classes.custom.SimpleCurrency;
import org.mtomczyk.typescript.generator.Mapper;

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
