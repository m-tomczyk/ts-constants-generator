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
package dev.mtomczyk.typescript.generator;

import dev.mtomczyk.typescript.generator.classes.clean.*;
import dev.mtomczyk.typescript.generator.classes.clean.dup.SingleConstantClass;
import dev.mtomczyk.typescript.generator.classes.inheritance.Child;
import dev.mtomczyk.typescript.generator.classes.inheritance.Parent;
import dev.mtomczyk.typescript.generator.results.GenerationResult;
import org.junit.Assert;
import org.junit.Test;

public class GenerateConstantCleanTest {

    private Generator setupGenerator() {
        return new Generator(true, false);
    }

    @Test
    public void testHasResult() {
        GenerationResult result = setupGenerator().generateFor(dev.mtomczyk.typescript.generator.classes.clean.SingleConstantClass.class);

        Assert.assertNotNull(result);
    }

    @Test
    public void testDuplicationException() {
        try {
            setupGenerator().generateFor(TestUtils.makeClassSet(SingleConstantClass.class, dev.mtomczyk.typescript.generator.classes.clean.SingleConstantClass.class));
            Assert.fail("Duplicated constant names not detected");
        } catch (IllegalStateException ignored) {
        }
    }

    @Test
    public void testRecognizedOnlyConstantsInClass() {
        GenerationResult result = setupGenerator().generateFor(MixedConstantClass.class);

        Assert.assertEquals(TestUtils.getFile("files/mixed/mixed-constant-class.d.ts", getClass().getClassLoader()), result.getDeclarationFile());
        Assert.assertEquals(TestUtils.getFile("files/mixed/mixed-constant-class.js", getClass().getClassLoader()), result.getImplementationFile());
    }

    @Test
    public void testSingleConstant() {
        GenerationResult result = setupGenerator().generateFor(dev.mtomczyk.typescript.generator.classes.clean.SingleConstantClass.class);

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


    @Test
    public void testInheritanceIgnored() {
        GenerationResult child = setupGenerator().generateFor(Child.class);
        GenerationResult parent = setupGenerator().generateFor(Parent.class);

        Assert.assertEquals(TestUtils.getFile("files/inheritance/empty-class.d.ts", getClass().getClassLoader()), child.getDeclarationFile());
        Assert.assertEquals(TestUtils.getFile("files/inheritance/empty-class.js", getClass().getClassLoader()), child.getImplementationFile());

        Assert.assertEquals(TestUtils.getFile("files/inheritance/inheritance-class.d.ts", getClass().getClassLoader()), parent.getDeclarationFile());
        Assert.assertEquals(TestUtils.getFile("files/inheritance/inheritance-class.js", getClass().getClassLoader()), parent.getImplementationFile());
    }

    @Test
    public void testArrayConstants() {
        GenerationResult result = setupGenerator().generateFor(ArrayConstantClass.class);

        Assert.assertEquals(TestUtils.getFile("files/array/array-constant-class.d.ts", getClass().getClassLoader()), result.getDeclarationFile());
        Assert.assertEquals(TestUtils.getFile("files/array/array-constant-class.js", getClass().getClassLoader()), result.getImplementationFile());
    }
}
