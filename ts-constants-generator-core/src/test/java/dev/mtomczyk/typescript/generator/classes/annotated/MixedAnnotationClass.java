package dev.mtomczyk.typescript.generator.classes.annotated;

import dev.mtomczyk.typescript.generator.TypescriptConstant;

public class MixedAnnotationClass {
    public static final String NO_ANNOTATION_CONST = "VALUE";
    @TypescriptConstant
    public static final String ANNOTATION_CONST = "VALUE";
}
