package org.mtomczyk.typescript.generator;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import org.mtomczyk.typescript.generator.classes.annotated.ChangeNameClass;
import org.mtomczyk.typescript.generator.classes.annotated.MixedAnnotationClass;
import org.mtomczyk.typescript.generator.classes.annotated.SingleConstantClass;
import org.mtomczyk.typescript.generator.results.GenerationResult;

import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GenerateConstantsAnnotationTest {

    private final boolean verbose = true;
    private final boolean annotationMode = true;

    @Test
    public void testSingleConstantAnnotated() {
        GenerationResult result = getGenerator().generateFor(SingleConstantClass.class);

        Assert.assertEquals(TestUtils.getFile("files/single/single-constant-class.d.ts", getClass().getClassLoader()), result.getDeclarationFile());
        Assert.assertEquals(TestUtils.getFile("files/single/single-constant-class.js", getClass().getClassLoader()), result.getImplementationFile());
    }

    @NotNull
    private Generator getGenerator() {
        return new Generator(verbose, annotationMode);
    }

    @Test
    public void testRecognizeOnlyAnnotated() {
        Supplier<Stream<FieldWithName>> result = getGenerator().getConstantFields(MixedAnnotationClass.class);
        String constants = result.get().map(FieldWithName::getName).collect(Collectors.joining(","));

        Assert.assertEquals("ANNOTATION_CONST", constants);
    }

    @Test
    public void testShouldRewriteNameInAnnotation() {
        Supplier<Stream<FieldWithName>> result = getGenerator().getConstantFields(ChangeNameClass.class);
        String constants = result.get().map(FieldWithName::getName).collect(Collectors.joining(","));

        Assert.assertEquals("NEW_NAME", constants);
    }
}
