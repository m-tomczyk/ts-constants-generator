package dev.mtomczyk.typescript.generator;

import dev.mtomczyk.typescript.generator.classes.custom.SimpleCurrencyClass;
import dev.mtomczyk.typescript.generator.classes.custom.mappers.SimpleCurrencyMapper;
import dev.mtomczyk.typescript.generator.results.GenerationResult;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class GenerateConstantsMappersTest {

    private Generator setupGenerator() {
        Set<Mapper> mappers = new HashSet<>();
        mappers.add(new SimpleCurrencyMapper());

        return new Generator(mappers, true, false);
    }

    @Test
    public void testCanUseMapper() {
        GenerationResult result = setupGenerator().generateFor(SimpleCurrencyClass.class);

        Assert.assertEquals(TestUtils.getFile("files/custom/simple-currency-class.d.ts", getClass().getClassLoader()), result.getDeclarationFile());
        Assert.assertEquals(TestUtils.getFile("files/custom/simple-currency-class.js", getClass().getClassLoader()), result.getImplementationFile());

    }
}
