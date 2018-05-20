/*
 * MIT License
 *
 * Copyright (c) 2018 Marcin Tomczyk https://github.com/m-tomczyk
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.mtomczyk.typescript.generator;

import org.junit.Assert;
import org.junit.Test;
import org.mtomczyk.typescript.generator.classes.clean.*;
import org.mtomczyk.typescript.generator.results.GenerationResult;

import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GenerateConstantCleanTest {

    private Generator setupGenerator() {
        return new Generator(true, false);
    }

    @Test
    public void testHasResult() {
        GenerationResult result = setupGenerator().generateFor(SingleConstantClass.class);

        Assert.assertNotNull(result);
    }

    @Test
    public void testRecognizedConstantsInClass() {
        Supplier<Stream<FieldWithName>> constantFields = setupGenerator().getConstantFields(MixedConstantClass.class);

        String classConstantNames = String.join(",", constantFields.get()
                .map(FieldWithName::getName)
                .collect(Collectors.toList()));

        Assert.assertEquals("STRING_CONSTANT,INTEGER_CONSTANT", classConstantNames);
    }

    @Test
    public void testDuplicationException() {
        try {
            setupGenerator().generateFor(TestUtils.makeClassSet(SingleConstantDupClass.class, SingleConstantClass.class));
            Assert.fail("Duplicated constant names not detected");
        } catch (IllegalStateException ignored) {
        }
    }

    @Test
    public void testSingleConstantImplementation() {
        Generator tsCG = setupGenerator();

        Assert.assertEquals("exports.CONSTANT_NAME = 'CONSTANT_VALUE';",
                tsCG.getConstantResult(tsCG.getConstantFields(SingleConstantClass.class).get())
                        .findFirst()
                        .orElseThrow(RuntimeException::new)
                        .getImplementation());
    }

    @Test
    public void testSingleConstantDeclaration() {
        Generator tsCG = setupGenerator();

        Assert.assertEquals("export declare const CONSTANT_NAME: string;",
                tsCG.getConstantResult(tsCG.getConstantFields(SingleConstantClass.class).get())
                        .findFirst()
                        .orElseThrow(RuntimeException::new)
                        .getDeclaration());
    }

    @Test
    public void testSingleConstant() {
        GenerationResult result = setupGenerator().generateFor(SingleConstantClass.class);

        Assert.assertEquals(TestUtils.getFile("files/single/single-constant-class.d.ts", getClass().getClassLoader()), result.getDeclarationFile());
        Assert.assertEquals(TestUtils.getFile("files/single/single-constant-class.js", getClass().getClassLoader()), result.getImplementationFile());
    }


    @Test
    public void testMultipleConstant() {
        GenerationResult result = setupGenerator().generateFor(MultipleConstantClass.class);

        Assert.assertEquals(TestUtils.getFile("files/multiple/multiple-constant-class.d.ts", getClass().getClassLoader()), result.getDeclarationFile());
        Assert.assertEquals(TestUtils.getFile("files/multiple/multiple-constant-class.js", getClass().getClassLoader()), result.getImplementationFile());
    }

    @Test
    public void testBooleanConstant() {
        GenerationResult result = setupGenerator().generateFor(BooleanConstantClass.class);

        Assert.assertEquals(TestUtils.getFile("files/bool/boolean-constant-class.d.ts", getClass().getClassLoader()), result.getDeclarationFile());
        Assert.assertEquals(TestUtils.getFile("files/bool/boolean-constant-class.js", getClass().getClassLoader()), result.getImplementationFile());
    }

    @Test
    public void testMultipleTypeConstants() {
        GenerationResult result = setupGenerator().generateFor(MultipleTypeConstantClass.class);

        Assert.assertEquals(TestUtils.getFile("files/mtype/multiple-type-constant-class.d.ts", getClass().getClassLoader()), result.getDeclarationFile());
        Assert.assertEquals(TestUtils.getFile("files/mtype/multiple-type-constant-class.js", getClass().getClassLoader()), result.getImplementationFile());
    }


}
