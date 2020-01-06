package dev.mtomczyk.typescript.generator.generators;

import dev.mtomczyk.typescript.generator.*;
import dev.mtomczyk.typescript.generator.results.FieldResult;
import dev.mtomczyk.typescript.generator.results.SubGenerationResult;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FieldGenerator {
    private final Set<Mapper> mappers;
    private final boolean verbose;
    private final boolean annotationMode;

    public FieldGenerator(Set<Mapper> mappers, boolean verbose, boolean annotationMode) {
        this.mappers = mappers;
        this.verbose = verbose;
        this.annotationMode = annotationMode;
    }

    public SubGenerationResult generate(final Set<Class<?>> clazzes) {
        return resultFor(clazzes);
    }

    private SubGenerationResult resultFor(final Set<Class<?>> clazzes) {
        final List<FieldWithName> fields = getConstantFields(clazzes).get().collect(Collectors.toList());
        System.out.println("Found " + fields.size() + " fields considered as constants");
        checkForDuplicatesAndThrow(fields.stream());

        return new SubGenerationResult(getConstantResult(fields.stream()).collect(Collectors.toSet()));
    }

    private Supplier<Stream<FieldWithName>> getConstantFields(final Set<Class<?>> clazzes) {
        return () -> clazzes.stream()
                .flatMap(aClass -> getConstantFields(aClass)
                        .get()
                        .filter(fieldWithName -> fieldWithName.getField().getDeclaringClass().equals(aClass))
                );
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

                    return new FieldWithName(clazz.getSimpleName() + '$' +name, field);
                });
    }

    Stream<FieldResult> getConstantResult(final Stream<FieldWithName> stream) {
        return stream
                .map(field -> {
                    try {
                        //noinspection unchecked
                        return new FieldResult(
                                field.getName(),
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

}
