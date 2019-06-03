package dev.mtomczyk.typescript.generator;

import dev.mtomczyk.typescript.generator.generators.FieldGenerator;
import dev.mtomczyk.typescript.generator.mapper.*;
import dev.mtomczyk.typescript.generator.results.GenerationResult;
import dev.mtomczyk.typescript.generator.results.SubGenerationResult;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

final class Generator {

    private final Set<Mapper> mappers = new HashSet<>();
    private final boolean verbose;
    private final boolean annotationMode;

    Generator() {
        this(true);
    }

    Generator(boolean annotationMode) {
        this(false, annotationMode);
    }

    Generator(final boolean verbose, boolean annotationMode) {
        this(new HashSet<>(), verbose, annotationMode);
    }

    Generator(final Set<Mapper> mappers, boolean annotationMode) {
        this(mappers, false, annotationMode);
    }

    Generator(final Set<Mapper> mappers, final boolean verbose, boolean annotationMode) {
        this.verbose = verbose;
        this.annotationMode = annotationMode;
        this.mappers.add(new StringMapper());
        this.mappers.add(new BooleanMapper());
        this.mappers.add(new ByteMapper());
        this.mappers.add(new DoubleMapper());
        this.mappers.add(new FloatMapper());
        this.mappers.add(new IntegerMapper());
        this.mappers.add(new LongMapper());
        this.mappers.add(new ShortMapper());
        if (verbose) {
            final String internalMappers = this.mappers.stream().map(Object::toString).collect(Collectors.joining(", "));
            final String externalMappers = mappers.stream().map(Object::toString).collect(Collectors.joining(", "));
            System.out.println("Using builtin mappers: " + internalMappers);
            System.out.println("Using external mappers: " + externalMappers);
        }

        this.mappers.addAll(mappers);
    }

    GenerationResult generateFor(final Class<?> clazz) {
        return generateFor(Stream.of(clazz).collect(Collectors.toSet()));
    }

    GenerationResult generateFor(final Set<Class<?>> classes) {
        System.out.println("Generating constants from " + classes.size() + " classes");
        final SubGenerationResult fieldResult = (new FieldGenerator(mappers, verbose, annotationMode)).generate(classes);

        return new GenerationResult(
                fieldResult.getDeclarations(),
                (new StringBuilder(Utils.createJsFileHeader())).append(fieldResult.getImplementations()).toString());
    }


}
