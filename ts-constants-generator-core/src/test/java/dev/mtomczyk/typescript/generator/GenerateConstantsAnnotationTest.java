package dev.mtomczyk.typescript.generator;

import dev.mtomczyk.typescript.generator.classes.annotated.ChangeNameClass;
import dev.mtomczyk.typescript.generator.classes.annotated.MixedAnnotationClass;
import dev.mtomczyk.typescript.generator.classes.annotated.SingleConstantClass;
import dev.mtomczyk.typescript.generator.results.GenerationResult;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

public class GenerateConstantsAnnotationTest {

    @NotNull
    private Generator getGenerator() {
        return new Generator(true, true);
    }

    @Test
    public void testSingleConstantAnnotated() {
        GenerationResult result = getGenerator().generateFor(SingleConstantClass.class);

        Assert.assertEquals(TestUtils.getFile("files/single/single-constant-class.d.ts", getClass().getClassLoader()), result.getDeclarationFile());
        Assert.assertEquals(TestUtils.getFile("files/single/single-constant-class.js", getClass().getClassLoader()), result.getImplementationFile());
    }

    @Test
    public void testRecognizeOnlyAnnotated() {
        GenerationResult result = getGenerator().generateFor(MixedAnnotationClass.class);

        Assert.assertEquals(TestUtils.getFile("files/ignore/ignore-constant-class.d.ts", getClass().getClassLoader()), result.getDeclarationFile());
        Assert.assertEquals(TestUtils.getFile("files/ignore/ignore-constant-class.js", getClass().getClassLoader()), result.getImplementationFile());
    }

    @Test
    public void testShouldRewriteNameInAnnotation() {

        GenerationResult result = getGenerator().generateFor(ChangeNameClass.class);

        Assert.assertEquals(TestUtils.getFile("files/change_name/change-name-constant-class.d.ts", getClass().getClassLoader()), result.getDeclarationFile());
        Assert.assertEquals(TestUtils.getFile("files/change_name/change-name-constant-class.js", getClass().getClassLoader()), result.getImplementationFile());
    }
}
