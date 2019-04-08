package dev.mtomczyk.typescript.generator.classes.annotated;

import dev.mtomczyk.typescript.generator.TypescriptConstant;

public class ChangeNameClass {
    @TypescriptConstant("NEW_NAME")
    public static final String THIS_NAME_WILL_BE_OVERRIDEN = "VALUE";
}
