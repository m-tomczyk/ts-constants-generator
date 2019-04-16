package dev.mtomczyk.typescript.generator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

class TestUtils {

    static String getFile(String name, ClassLoader classLoader) {
        String fileContents = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream(name)))
                .lines().collect(Collectors.joining(System.lineSeparator()));
        return fileContents.isEmpty() ? fileContents : fileContents + System.lineSeparator(); // TODO do not add this line separator. Find out why this drops last empty line
    }

    static LinkedHashSet<Class<?>> makeClassSet(Class<?>... items) {
        return new LinkedHashSet<>(Arrays.asList(items));
    }
}
