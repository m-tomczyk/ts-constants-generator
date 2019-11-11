package dev.mtomczyk.typescript.generator.generators;

import dev.mtomczyk.typescript.generator.*;
import dev.mtomczyk.typescript.generator.results.FieldResult;
import dev.mtomczyk.typescript.generator.results.SubGenerationResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public final class ClassAnnotationGenerator {

    private final Set<Mapper> mappers;
    private final boolean verbose;
    private final boolean annotationMode;

    public ClassAnnotationGenerator(Set<Mapper> mappers, boolean verbose, boolean annotationMode) {
        this.mappers = mappers;
        this.verbose = verbose;
        this.annotationMode = annotationMode;
    }

    public SubGenerationResult generate(final Set<Class<?>> clazzes) {
        Set<FieldResult> todo = clazzes.stream()
                .filter(clazz -> annotationMode && clazz.isAnnotationPresent(TypescriptAnnotationConstant.class))
                .flatMap(clazz -> {
                    TypescriptAnnotationConstant typescriptAnnotationConstant = clazz.getDeclaredAnnotation(TypescriptAnnotationConstant.class);
                    Annotation properAnnotation = clazz.getDeclaredAnnotation(typescriptAnnotationConstant.value());

                    return Arrays.stream(properAnnotation.getClass()
                            .getDeclaredMethods())
                            .filter(method -> method.getName().equals(typescriptAnnotationConstant.annotationField()))
                            .map(method -> {
                                try {
                                    String name = !typescriptAnnotationConstant.name().isEmpty()
                                            ? typescriptAnnotationConstant.name()
                                            : clazz.getSimpleName() + "$" + properAnnotation.annotationType().getSimpleName();
                                    //noinspection unchecked
                                    return new FieldResult(
                                            name,
                                            TsConstructs.emmitConstantDeclaration(
                                                    name,
                                                    getConstantType(method.getReturnType())),
                                            TsConstructs.emmitConstantImplementation(
                                                    name,
                                                    getMapperByType(method.getReturnType()).getValue(method.invoke(properAnnotation)))
                                    );
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    throw new IllegalStateException("Cannot get annotation value", e);
                                }
                            });
                }).collect(Collectors.toSet());


        return new SubGenerationResult(todo);
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

}
