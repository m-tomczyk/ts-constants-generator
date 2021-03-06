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
package dev.mtomczyk.typescript.generator.mapper;

import dev.mtomczyk.typescript.generator.Mapper;
import dev.mtomczyk.typescript.generator.TsType;

public class DoubleMapper implements Mapper<Double> {
    @Override
    public Class<Double> getMappedType() {
        return Double.class;
    }

    @Override
    public TsType getTsType() {
        return TsType.NUMBER;
    }

    @Override
    public String getValue(Double object) {
        return String.valueOf(object.doubleValue());
    }

    @Override
    public String toString() {
        return "DoubleMapper";
    }
}
