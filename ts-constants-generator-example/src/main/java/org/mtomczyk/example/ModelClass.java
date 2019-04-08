package org.mtomczyk.example;

import org.mtomczyk.typescript.generator.TypescriptConstant;

public class ModelClass {
    @TypescriptConstant
    public static final String EXAMPLE = "EXAMPLE";

    private String name;
    private int age;

    public ModelClass(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
