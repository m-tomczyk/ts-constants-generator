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
package org.mtomczyk.typescript.generator;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

import java.io.File;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;

/**
 * Class provides entry suggested entry point to Typescript constant generator as builder.
 */
@PublicApi
public class TsConstantsGeneratorBuilder {
    private static final String DEFAULT_FILE_NAME = "constants";
    private static final String DEFAULT_FILE_NAME_STANDALONE = "index";
    private static final String DEFAULT_PATH = "ts" + File.separator + "constants";
    private static final boolean DEFAULT_STANDALONE = false;
    private static final boolean DEFAULT_ANNOTATION_MODE = true;
    private static final boolean DEFAULT_VERBOSE = false;
    private static final String DEFAULT_PACKAGE_NAME = "typescript-constants";
    private static final String DEFAULT_PACKAGE_VERSION = "1.0.0";

    // Class configuration
    private URLClassLoader classLoader;

    // Scan configuration
    private Set<Class<?>> classes = new HashSet<>();
    private Set<String> paths = new HashSet<>();
    private boolean annotationMode = DEFAULT_ANNOTATION_MODE;

    // Mappers configuration
    private Set<Mapper> mappers = new HashSet<>();
    private Set<String> mapperPaths = new HashSet<>();

    // Emmit configuration
    private String targetFilename;
    private String targetPath = DEFAULT_PATH;
    private boolean standalone = DEFAULT_STANDALONE;
    private String packageName = DEFAULT_PACKAGE_NAME;
    private String packageVersion = DEFAULT_PACKAGE_VERSION;

    // Common configuration
    private boolean verbose = DEFAULT_VERBOSE;

    /**
     * Supply class loader for paths and mapper paths scanning
     *
     * @param classLoader Class loader containing classes to scan
     * @return This builder instance
     */
    @PublicApi
    public TsConstantsGeneratorBuilder usingUrlClassLoader(final URLClassLoader classLoader) {
        this.classLoader = classLoader;
        return this;
    }

    /**
     * Supply class to scan for constants inside
     *
     * @param clazz Class to scan
     * @return This builder instance
     */
    @PublicApi
    public TsConstantsGeneratorBuilder scanningClass(final Class<?> clazz) {
        this.classes.add(clazz);
        return this;
    }

    /**
     * Supply classes to scan for constants inside
     *
     * @param classes Classes to scan
     * @return This builder instance
     */
    @PublicApi
    public TsConstantsGeneratorBuilder scanningClasses(final Set<Class<?>> classes) {
        this.classes.addAll(classes);
        return this;
    }

    /**
     * Supply path to scan for classes and constants inside
     *
     * @param path Path with classes containing constants
     * @return This builder instance
     */
    @PublicApi
    public TsConstantsGeneratorBuilder scanningPath(final String path) {
        this.paths.add(path);
        return this;
    }

    /**
     * Supply paths to scan for classes and constants inside
     *
     * @param paths Paths with classes containing constants
     * @return This builder instance
     */
    @PublicApi
    public TsConstantsGeneratorBuilder scanningPaths(final Set<String> paths) {
        this.paths.addAll(paths);
        return this;
    }

    /**
     * Scan only for fields containing {@link TypescriptConstant} annotation in classes supplied by paths
     *
     * @return This builder instance
     */
    @PublicApi
    public TsConstantsGeneratorBuilder useAnnotationMode() {
        this.annotationMode = true;
        return this;
    }

    /**
     * Scan all classes supplied by paths
     *
     * @return This builder instance
     */
    @PublicApi
    public TsConstantsGeneratorBuilder useWholeScanMode() {
        this.annotationMode = false;
        return this;
    }

    /**
     * Supply path containing mapper classes
     *
     * @param path Path of mapper classes
     * @return This builder instance
     */
    @PublicApi
    public TsConstantsGeneratorBuilder mapperPath(final String path) {
        this.mapperPaths.add(path);
        return this;
    }

    /**
     * Supply paths containing mapper classes
     *
     * @param paths Paths of mapper classes
     * @return This builder instance
     */
    @PublicApi
    public TsConstantsGeneratorBuilder mapperPaths(final Set<String> paths) {
        this.mapperPaths.addAll(paths);
        return this;
    }

    /**
     * Supply mapper for constant generation type
     *
     * @param mapper Constant mapper
     * @return This builder instance
     */
    @PublicApi
    public TsConstantsGeneratorBuilder mapper(final Mapper mapper) {
        this.mappers.add(mapper);
        return this;
    }

    /**
     * Supply mappers for constant generation type
     *
     * @param mappers Constant mappers
     * @return This builder instance
     */
    @PublicApi
    public TsConstantsGeneratorBuilder mappers(final Set<Mapper> mappers) {
        this.mappers.addAll(mappers);
        return this;
    }

    /**
     * Supply target file name
     *
     * @param targetFilename File name of target
     * @return This builder instance
     */
    @PublicApi
    public TsConstantsGeneratorBuilder withTargetFilename(final String targetFilename) {
        this.targetFilename = targetFilename;
        return this;
    }

    /**
     * Supply target path
     *
     * @param targetPath path to target directory
     * @return This builder instance
     */
    @PublicApi
    public TsConstantsGeneratorBuilder withTargetPath(final String targetPath) {
        this.targetPath = targetPath;
        return this;
    }

    /**
     * Generate package.json file
     *
     * @return This builder instance
     */
    @PublicApi
    public TsConstantsGeneratorBuilder useStandaloneMode() {
        this.standalone = true;
        return this;
    }

    /**
     * Do not generate package.json file. Use already existing or just emmit *.d.ts and *.js files
     *
     * @return This builder instance
     */
    @PublicApi
    public TsConstantsGeneratorBuilder useDependentOrOnlyFilesMode() {
        this.standalone = false;
        return this;
    }

    /**
     * Supply package name. Applicable only if {@link TsConstantsGeneratorBuilder#useStandaloneMode()} is used
     *
     * @param packageName Package name
     * @return This builder instance
     */
    @PublicApi
    public TsConstantsGeneratorBuilder withPackageName(final String packageName) {
        this.packageName = packageName;
        return this;
    }

    /**
     * Supply package version. Applicable only if {@link TsConstantsGeneratorBuilder#useStandaloneMode()} is used
     *
     * @param packageVersion Package version
     * @return This builder instance
     */
    @PublicApi
    public TsConstantsGeneratorBuilder withPackageVersion(final String packageVersion) {
        this.packageVersion = packageVersion;
        return this;
    }

    /**
     * Scan for classes and build Executor
     *
     * @return Executor class
     */
    @PublicApi
    public Executor build() {
        if (classLoader == null && (!paths.isEmpty() || !mapperPaths.isEmpty())) {
            throw new IllegalArgumentException("Cannot scan for classes whiteout supplied class loader");
        }
        if (standalone && targetFilename == null) {
            targetFilename = DEFAULT_FILE_NAME_STANDALONE;
        }
        if (!standalone && targetFilename == null) {
            targetFilename = DEFAULT_FILE_NAME;
        }

        Set<Class<?>> classes = new HashSet<>();
        if (!paths.isEmpty()) {
            classes.addAll(consolidateToClasses());
        }
        Set<Mapper> mappers = new HashSet<>();
        if (!mapperPaths.isEmpty()) {
            mappers.addAll(consolidateMappersToClasses());
        }

        return new Executor(targetPath, targetFilename, classes, mappers, packageName, packageVersion, standalone, verbose, targetPath, annotationMode);
    }

    private Set<Class<?>> consolidateToClasses() {
        final Set<Class<?>> consolidatedClasses = new HashSet<>(classes);

        final String pathsToScan[] = new String[paths.size()];

        FastClasspathScanner scanner = new FastClasspathScanner(paths.toArray(pathsToScan))
                .addClassLoader(classLoader);

        if (annotationMode) {
            scanner.matchClassesWithFieldAnnotation(TypescriptConstant.class, (aClass, field) -> consolidatedClasses.add(aClass)).scan();
        } else {
            scanner.matchAllStandardClasses(consolidatedClasses::add).scan();
        }

        return consolidatedClasses;
    }

    private Set<Mapper> consolidateMappersToClasses() {
        final Set<Mapper> consolidatedMappers = new HashSet<>(mappers);

        final String pathsToScan[] = new String[mapperPaths.size()];

        new FastClasspathScanner(mapperPaths.toArray(pathsToScan))
                .addClassLoader(classLoader)
                .matchClassesImplementing(Mapper.class, aClass -> {
                    try {
                        consolidatedMappers.add(aClass.newInstance());
                    } catch (InstantiationException | IllegalAccessException e) {
                        if (verbose) {
                            e.printStackTrace();
                        }

                        throw new IllegalArgumentException("Could not create custom mapper: "
                                + System.lineSeparator()
                                + e.getLocalizedMessage()
                                + System.lineSeparator()
                                + System.lineSeparator()
                                + "Remember that your mapper must have public no-argument constructor", e);
                    }
                }).scan();

        return consolidatedMappers;
    }

}
