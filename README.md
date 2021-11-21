# Typescript constants generator
**SEE FOOTNOTES BELOW!**

This library is a tool providing ability to generate pairs of Javascript and 
Typescript declaration files from ``public static final`` fields of Java classes.

Inspired and substitution to [typescript-generator](https://github.com/vojtechhabarta/typescript-generator).

## Purpose
For example, given Java class:
```java
public class ProjectConstants {
    @TypescriptConstant
    public static final String MY_API_ENDPOINT = "api/crud/method/x";
}
```

Output will consist of *.js file:

```javascript
"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.ProjectConstants$MY_API_ENDPOINT = 'api/crud/method/x';
```

And corresponding *.d.ts file:
```typescript
export declare const ProjectConstants$MY_API_ENDPOINT: string;
```

## Supported types
All java primitive types and their wrappers with addition of ``String`` are supported. 
Array versions of all those types are supported as well.

Additional type mappings can be defined using ``Mapper`` interface

## Usage examples
### Plain constants
For variable to be considered as constant it has to be prefixed with following `public static final` 
and annotated with `@TypescriptConstant`. By default, generated variable will always be named using 
this convention: `ClassName$VARIABLE_NAME`. 

Please note that because there is only single output file
name conflict could appear when two constants of the same name from two different classes of the same
name are visible to generator at same time (this will be reported and constants generation will fail).
This can be mitigated passing String as argument to annotation, overriding variable name.

Example of usage of various valid constant variables:
```java
public class MyConstants {
    @TypescriptConstant()
    public static final String API_ENDPOINT = "/api/v1.0/get_users"; // Will generate: MyConstants$API_ENDPOINT = "/api/v1.0/get_users"
    @TypescriptConstant()
    public static final Boolean IS_DEBUG_MODE_AVAILABLE = Boolean.TRUE; // Will generate: MyConstants$IMPORTANT_MAGIC_NUMBER = true
    @TypescriptConstant()
    public static final int IMPORTANT_MAGIC_NUMBER = 42; // Will generate: MyConstants$IMPORTANT_MAGIC_NUMBER = 42
    @TypescriptConstant()
    public static final int[] LOTTERY_NEXT_WINNING_NUMBERS = { 4, 8, 15, 16, 23, 42 }; // Will generate: MyConstants$LOTTERY_NEXT_WINNING_NUMBERS = [ 4, 8, 15, 16, 23, 42 ]
    @TypescriptConstant("THIS_IS_NICER_NAME_ANYWAY")
    public static final Double THIS_NAME_REPEATS_IN_EVERY_MY_CONSTANTS_CLASS = 1d; // Will generate: MyConstants$THIS_IS_NICER_NAME_ANYWAY = 1
}
```

### Annotation constants
Sometimes constants are used in class annotations and while you could create plain constant and like
shown above and supply this to annotations it can be tedious. This library provides shorthand for such
usage generating constants with following convention `MyClass$$AnnotationName:
```java
@TypescriptAnnoatationConstant(AnnotationName.class)
@AnnotationName("SOME_CONSTANT")
class MyClass {
} // Will generate: MyClass$$AnnotationName = "SOME_CONSTANT

@TypesctripAnnotationConstant(value = MoreComplexAnnoatation.class, name = "BETTER_NAME", annotationField = "name" )
@MoreComplexAnnoatation(value = 1, name = "SOMETHING")
class MyOtherClass {
} // Will generate: MyOtherClass&&BETTER_NAME = "SOMETHING"
```

## TypescriptConstant annotation
By default, project will scan only for fields annotated by @TypescriptConstant

## Maven plugin
Library provides maven plugin for code generation. Simple usage:

```xml
<plugin>
    <groupId>dev.mtomczyk</groupId>
    <artifactId>ts-constants-generator-maven</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <executions>
        <execution>
            <id>generate</id>
            <goals>
                <goal>generate</goal>
            </goals>
            <phase>process-classes</phase>
        </execution>
    </executions>
    <configuration>
        <paths>
            <path>dev.mtomczyk.example</path>
        </paths>
        <verbose>true</verbose>
        <annotationMode>true</annotationMode>
        <targetFileName>constants</targetFileName>
        <standaloneMode>true</standaloneMode>
        <targetPath>ts</targetPath>
        <mappings>
            <mapping>dev.mtomczyk.example</mapping>
        </mappings>
    </configuration>
</plugin>
```

## Footnote
Library is in development period. Project will use semver. Additionally for versions <1.0.0
any change in y (x.y.z) may contain breaking changes in public api (Classes and methods annotated by @PublicApi).

For now you need to compile from sources by yourself - not available in maven central.