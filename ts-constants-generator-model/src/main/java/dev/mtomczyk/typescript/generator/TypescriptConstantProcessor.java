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

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import java.util.Set;

/**
 * TypescriptConstant processor that ensures usage on proper fields.
 */
@SupportedAnnotationTypes("TypescriptConstant")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class TypescriptConstantProcessor extends AbstractProcessor {

    private ProcessingEnvironment env;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        this.env = env;
    }

    @Override
    public boolean process(final Set<? extends TypeElement> set, final RoundEnvironment roundEnvironment) {
        roundEnvironment.getElementsAnnotatedWith(TypescriptConstant.class)
                .stream()
                .filter(item -> !isVariableElement(item) || !isConstantElement((VariableElement) item))
                .forEach(item -> env.getMessager().printMessage(Diagnostic.Kind.ERROR,
                        String.format("Field annotated as @TypescriptConstant should always be declared as " +
                                        "public static final but field called '%s' from class '%s' is not",
                                item.getSimpleName(),
                                resolveCanonicalClassName(item)
                        )));


        return true; // Finished processing annotations
    }

    private String resolveCanonicalClassName(Element item) {
        return env.getElementUtils().getPackageOf(item).getQualifiedName() + "." + item.getEnclosingElement().getSimpleName();
    }

    private boolean isVariableElement(Element item) {
        return item instanceof VariableElement;
    }

    // TODO move to utils and share with core?
    private boolean isConstantElement(VariableElement element) {
        Set<Modifier> modifiers = element.getModifiers();
        return modifiers.contains(Modifier.PUBLIC) && modifiers.contains(Modifier.STATIC) && modifiers.contains(Modifier.FINAL);
    }

}
