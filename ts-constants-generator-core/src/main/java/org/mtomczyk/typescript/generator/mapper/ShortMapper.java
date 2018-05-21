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
package org.mtomczyk.typescript.generator.mapper;

import org.mtomczyk.typescript.generator.Mapper;
import org.mtomczyk.typescript.generator.TsType;

public class ShortMapper implements Mapper<Short> {
    @Override
    public Class<Short> getMappedType() {
        return Short.class;
    }

    @Override
    public TsType getTsType() {
        return TsType.NUMBER;
    }

    @Override
    public String getValue(Short object) {
        return String.valueOf(object.shortValue());
    }

    @Override
    public String toString() {
        return "ShortMapper";
    }
}
