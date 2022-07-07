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

import java.lang.reflect.Array;

public class Utils {
    public static Class<?> toWrapperClass(final Class<?> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }

        final Class<?> checkType = type.isArray() ? type.getComponentType() : type;

        if (checkType == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }

        if (!checkType.isPrimitive())
            return checkType;
        else if (int.class.equals(checkType))
            return Integer.class;
        else if (double.class.equals(checkType))
            return Double.class;
        else if (char.class.equals(checkType))
            return Character.class;
        else if (boolean.class.equals(checkType))
            return Boolean.class;
        else if (long.class.equals(checkType))
            return Long.class;
        else if (float.class.equals(checkType))
            return Float.class;
        else if (short.class.equals(checkType))
            return Short.class;
        else if (byte.class.equals(checkType))
            return Byte.class;
        else
            throw new IllegalArgumentException("Primitive type not supported: " + checkType.getName());
    }

    public static String createJsFileHeader() {
        return "\"use strict\";" +
                System.lineSeparator() +
                "Object.defineProperty(exports, \"__esModule\", { value: true });" +
                System.lineSeparator();
    }

    public static Object[] unpack(Object array) {
        Object[] result = new Object[Array.getLength(array)];
        for (int i = 0; i < result.length; i++) {
            result[i] = Array.get(array, i);
        }
        return result;
    }
}
