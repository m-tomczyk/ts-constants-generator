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

import com.google.gson.GsonBuilder;
import dev.mtomczyk.typescript.generator.results.GenerationResult;
import dev.mtomczyk.typescript.generator.packagejson.PackageJson;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

class Emitter {
    private final String path;
    private final boolean verbose;

    Emitter(String path, boolean verbose) {
        this.path = path;
        this.verbose = verbose;
    }

    void emmit(GenerationResult result, String name) {
        writeDeclarationFile(name, result.getDeclarationFile());
        writeImplementationFile(name, result.getImplementationFile());
    }

    void emmitPackage(PackageJson packageJson) {
        checkForPath();

        try {
            final String packageJsonGenerated = new GsonBuilder()
                    .setPrettyPrinting()
                    .create()
                    .toJson(packageJson);
            final String packageJsonFilePath = path + File.separator + TsConstructs.FILE_NAME_EXT_PACKAGE_JSON;
            System.out.println("Writing npm " + TsConstructs.FILE_NAME_EXT_PACKAGE_JSON + " file");
            if (verbose) {
                System.out.println(packageJsonFilePath);
            }

            Files.write(Paths.get(packageJsonFilePath), packageJsonGenerated.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            if (verbose) {
                e.printStackTrace();
            }

            throw new IllegalStateException("Failed to write " + TsConstructs.FILE_NAME_EXT_PACKAGE_JSON + " file");
        }
    }

    private void writeImplementationFile(String name, String fileContent) {
        checkForPath();

        try {
            final String implementationFilePath = path + File.separator + name + TsConstructs.FILE_EXT_JS;
            System.out.println("Writing implementation file " + implementationFilePath);
            if (verbose) {
                System.out.println(fileContent);
            }

            Files.write(Paths.get(implementationFilePath), fileContent.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            if (verbose) {
                e.printStackTrace();
            }
            throw new IllegalStateException("Failed to write javascript implementation file " + name);
        }
    }

    private void writeDeclarationFile(String name, String fileContent) {
        checkForPath();

        try {
            final String declarationFilePath = path + File.separator + name + TsConstructs.FILE_EXT_D_TS;
            System.out.println("Writing declaration file " + declarationFilePath);
            if (verbose) {
                System.out.println(fileContent);
            }

            Files.write(Paths.get(declarationFilePath), fileContent.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            if (verbose) {
                e.printStackTrace();
            }
            throw new IllegalStateException("Failed to write typescript declaration file " + name);
        }
    }

    private void checkForPath() {
        File directory = new File(path);
        if (!directory.exists()) {
            boolean mkdirs = directory.mkdirs();
            System.out.println("Path did not existed. Created it");
        }
    }
}
