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

import dev.mtomczyk.typescript.generator.results.GenerationResult;
import dev.mtomczyk.typescript.generator.packagejson.PackageJson;

import java.util.HashSet;
import java.util.Set;

@PublicApi
public class Executor {
    private static final int NANO_TO_MILLISECONDS_DENOMINATOR = 1000000;

    private final Generator generator;
    private final Emitter emitter;

    private final Set<Class<?>> classes = new HashSet<>();
    private final String targetFilename;
    private final String packageName;
    private final String packageVersion;
    private final boolean standalone;
    private final String targetPath;

    Executor(String path,
             String targetFilename,
             Set<Class<?>> classes,
             Set<Mapper> mappers,
             String packageName,
             String packageVersion,
             boolean standalone,
             boolean verbose,
             String targetPath,
             boolean annotationMode) {
        this.packageName = packageName;
        this.packageVersion = packageVersion;
        this.standalone = standalone;
        this.targetPath = targetPath;
        this.classes.addAll(classes);
        this.targetFilename = targetFilename;

        this.generator = new Generator(mappers, verbose, annotationMode);
        this.emitter = new Emitter(path, verbose);
    }

    @PublicApi
    public void execute() {
        System.out.println("Typescript constants generator starting execution");
        long startTime = System.nanoTime();

        validate();
        GenerationResult generationResult = this.generator.generateFor(classes);
        this.emitter.emmit(generationResult, targetFilename);

        if (standalone) {
            PackageJson packageJson = new PackageJson(packageName, packageVersion, targetFilename + TsConstructs.FILE_EXT_JS, targetFilename + TsConstructs.FILE_EXT_D_TS);
            this.emitter.emmitPackage(packageJson);
        }
        long finishTime = System.nanoTime();

        System.out.println("Constants generation done. Elapsed time in milliseconds: " + (finishTime - startTime) / NANO_TO_MILLISECONDS_DENOMINATOR);
    }

    private void validate() {
        if (targetPath == null || targetPath.trim().isEmpty()) {
            throw new IllegalArgumentException("Target path cannot be null or empty");
        }

        if (targetFilename == null || targetFilename.trim().isEmpty()) {
            throw new IllegalArgumentException("Target filename cannot be null or empty");
        }

        if (standalone && packageName == null) {
            throw new IllegalArgumentException("Package name cannot be null in standalone mode");
        }

        if (standalone && packageVersion == null) {
            throw new IllegalArgumentException("Package version cannot be null in standalone mode");
        }
    }
}
