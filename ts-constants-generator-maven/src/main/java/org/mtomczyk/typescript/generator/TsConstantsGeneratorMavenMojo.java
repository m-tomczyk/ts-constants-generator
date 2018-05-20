/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Generates Typescript declaration and implementation files with constants from given java classes.
 */
@Mojo(name = TsConstantsGeneratorMavenMojo.GOAL_NAME, defaultPhase = LifecyclePhase.PROCESS_CLASSES, requiresDependencyResolution = ResolutionScope.COMPILE)
public class TsConstantsGeneratorMavenMojo extends AbstractMojo {
    static final String GOAL_NAME = "generate";

    /**
     * Target path for generated typescript files
     */
    @Parameter
    private String targetPath;

    /**
     * Name for constant declaration and implementation files
     */
    @Parameter
    private String targetFileName;

    /**
     * Package name of node module
     */
    @Parameter
    private String packageName;

    /**
     * Package version of node module
     */
    @Parameter
    private String packageVersion;

    /**
     * Should output of plugin be more verbose
     */
    @Parameter
    private boolean verbose;

    /**
     * Should generator use only fields annotated as TypescriptConstant
     */
    @Parameter
    private boolean annotationMode;

    /**
     * Paths for classes scanning
     */
    @Parameter(required = true)
    private Set<String> paths;

    /**
     * Paths for mappers scanning
     */
    @Parameter
    private Set<String> mappers;

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    public void execute() throws MojoExecutionException {

        System.out.println("Starting typescript constants generator maven plugin");

        try {

            List<URL> projectClasspathList = new ArrayList<>();
            for (String element : project.getCompileClasspathElements()) {
                try {
                    projectClasspathList.add(new File(element).toURI().toURL());
                } catch (MalformedURLException e) {
                    throw new MojoExecutionException(element + " is an invalid classpath element", e);
                }
            }

            URLClassLoader loader = new URLClassLoader(projectClasspathList.toArray(new URL[0]), Thread.currentThread().getContextClassLoader());

            TsConstantsGeneratorBuilder builder = new TsConstantsGeneratorBuilder();
            builder.usingUrlClassLoader(loader);
            builder.scanningPaths(paths);
            if (mappers != null) {
                builder.mapperPaths(mappers);
            }
            builder.useStandaloneMode();
            if (packageName != null) {
                builder.withPackageName(packageName);
            }
            if (packageVersion != null) {

                builder.withPackageVersion(packageVersion);
            }
            if (targetFileName != null) {

                builder.withTargetFilename(targetFileName);
            }
            if (targetPath != null) {

                builder.withTargetPath(targetPath);
            }
            if (annotationMode) {
                builder.useAnnotationMode();
            } else {
                builder.useWholeScanMode();
            }
            Executor executor = builder.build();
            executor.execute();

        } catch (DependencyResolutionRequiredException e) {
            throw new MojoExecutionException("Dependency resolution failed", e);
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new MojoExecutionException("Failed to generate constants", e);
        }
    }
}
