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
package dev.mtomczyk.typescript.generator.generators;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

class FieldWithName implements Comparable<FieldWithName> {
    private final String name;
    private final Field field;

    FieldWithName(String name, Field field) {
        this.name = name;
        this.field = field;
    }

    String getName() {
        return name;
    }

    Field getField() {
        return field;
    }

    @Override
    public int compareTo(@NotNull FieldWithName fieldWithName) {
        return name.compareTo(fieldWithName.name);
    }
}
