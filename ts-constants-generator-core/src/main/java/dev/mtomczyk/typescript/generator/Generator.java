package dev.mtomczyk.typescript.generator;

import dev.mtomczyk.typescript.generator.mapper.*;
import dev.mtomczyk.typescript.generator.mapper.*;
import dev.mtomczyk.typescript.generator.results.FieldResult;
import dev.mtomczyk.typescript.generator.results.GenerationResult;
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
import dev.mtomczyk.typescript.generator.results.SubGenerationResult;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        final SubGenerationResult fieldResult = resultFor(classes);

        return new GenerationResult(
                fieldResult.getDeclarations(),
                (new StringBuilder(Utils.createJsFileHeader())).append(fieldResult.getImplementations()).toString());
    }

    Supplier<Stream<FieldWithName>> getConstantFields(final Class<?> clazz) {
        return () -> Arrays.stream(clazz.getFields())
                .filter(item -> {
                    final int modifiers = item.getModifiers();
                    boolean isConstant = Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers);
                    if (verbose) {
                        final String result = isConstant ? "is recognized as constant" : "is rejected";
                        System.out.println("For class " + clazz.getCanonicalName() + " field " + item.getName() + " " + result);
                    }

                    if (annotationMode) {
                        TypescriptConstant annotation = item.getAnnotation(TypescriptConstant.class);
                        return annotation != null && isConstant;
                    }
                    return isConstant;
                })
                .map(field -> {
                    final String name;
                    TypescriptConstant annotation = field.getAnnotation(TypescriptConstant.class);
                    if (annotationMode && annotation != null && !annotation.value().isEmpty()) {
                        name = annotation.value();
                    } else {
                        name = field.getName();
                    }

                    return new FieldWithName(name, field);
                });
    }

    Stream<FieldResult> getConstantResult(final Stream<FieldWithName> stream) {
        return stream
                .map(field -> {
                    try {
                        //noinspection unchecked
                        return new FieldResult(
                                TsConstructs.emmitConstantDeclaration(
                                        field.getName(),
                                        getConstantType(field.getField().getType())),
                                TsConstructs.emmitConstantImplementation(
                                        field.getName(),
                                        getMapperByType(field.getField().getType()).getValue(field.getField().get(null)))
                        );
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private SubGenerationResult resultFor(final Set<Class<?>> clazzes) {
        final StringBuilder declaration = new StringBuilder();
        final StringBuilder implementation = new StringBuilder();

        final List<FieldWithName> fields = getConstantFields(clazzes).get().collect(Collectors.toList());
        System.out.println("Found " + fields.size() + " fields considered as constants");
        checkForDuplicatesAndThrow(fields.stream());

        Collections.sort(fields);

        getConstantResult(fields.stream()).forEach(fieldResult ->
        {
            declaration.append(fieldResult.getDeclaration()).append(System.lineSeparator());
            implementation.append(fieldResult.getImplementation()).append(System.lineSeparator());
        });

        return new SubGenerationResult(declaration.toString(), implementation.toString());
    }

    private Supplier<Stream<FieldWithName>> getConstantFields(final Set<Class<?>> clazzes) {
        return () -> clazzes.stream().flatMap(aClass -> getConstantFields(aClass).get());
    }

    private TsType getConstantType(final Class<?> type) {
        return getMapperByType(type).getTsType();
    }

    private Mapper getMapperByType(final Class<?> type) {
        return mappers.stream()
                .filter(mapper -> mapper.getMappedType() == Utils.toWrapperClass(type))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Cannot find mapper for type '" + type.getTypeName()));
    }

    private void checkForDuplicatesAndThrow(final Stream<FieldWithName> fields) {
        final Map<String, Set<Field>> groupedFields = new HashMap<>();

        fields.forEach(field -> {
            groupedFields.computeIfAbsent(field.getName(), k -> new HashSet<>());
            groupedFields.get(field.getName()).add(field.getField());
        });

        final List<Map.Entry<String, Set<Field>>> collect = groupedFields
                .entrySet()
                .stream()
                .filter(stringSetEntry -> stringSetEntry.getValue().size() > 1)
                .collect(Collectors.toList());

        if (collect.size() > 0) {

            final String errorMessage = collect.stream()
                    .map(stringSetEntry -> "Failed to create field mapping. Constant named '"
                            + stringSetEntry.getKey()
                            + "' appears in multiple classes: "
                            + stringSetEntry
                            .getValue()
                            .stream()
                            .map(field -> field.getDeclaringClass().getCanonicalName())
                            .collect(Collectors.joining(", ")))
                    .collect(Collectors.joining(System.lineSeparator()));

            throw new IllegalStateException(errorMessage);
        }
    }

}
