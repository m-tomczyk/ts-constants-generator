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
package dev.mtomczyk.typescript.generator.results;

import dev.mtomczyk.typescript.generator.Utils;

public class GenerationResult {

    private final String declarationFile;
    private final String implementationFile;

    public GenerationResult() {
        this.declarationFile = "";
        this.implementationFile = Utils.createJsFileHeader();
    }

    private GenerationResult(String declarationFile, String implementationFile, FieldResult fieldResult) {
        this.declarationFile = declarationFile.concat(fieldResult.getDeclaration()).concat(System.lineSeparator());
        this.implementationFile = implementationFile.concat(fieldResult.getImplementation()).concat(System.lineSeparator());
    }

    public GenerationResult append(FieldResult fieldResult) {
        return new GenerationResult(declarationFile, implementationFile, fieldResult);
    }

    public String getDeclarationFile() {
        return declarationFile;
    }

    public String getImplementationFile() {
        return implementationFile;
    }
}
