package dev.mtomczyk.typescript.generator;

import dev.mtomczyk.typescript.generator.classes.class_annotations.AnnotatedClass;
import dev.mtomczyk.typescript.generator.results.GenerationResult;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

public class GenerateConstantsFromAnnotations {

    @NotNull
    private Generator getGenerator() {
        return new Generator(true, true);
    }

    @Test
    public void testSingleConstantAnnotated() {
        GenerationResult result = getGenerator().generateFor(AnnotatedClass.class);

        Assert.assertEquals(TestUtils.getFile("files/class_annotations/simple-annotation-class.d.ts", getClass().getClassLoader()), result.getDeclarationFile());
        Assert.assertEquals(TestUtils.getFile("files/class_annotations/simple-annotation-class.js", getClass().getClassLoader()), result.getImplementationFile());
    }
}
