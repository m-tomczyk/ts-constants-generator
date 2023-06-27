# Typescript constants generator
**SEE FOOTNOTES BELOW!**

This library is a tool providing ability to generate pairs of *Javascript* and 
*Typescript declaration* files from ``public static final`` fields of Java classes and annotations.

Inspired and supplement to [typescript-generator](https://github.com/vojtechhabarta/typescript-generator).

## Purpose and basics
Given Java class:
```java
public class ProjectConstants {
    @TypescriptConstant
    public static final String MY_API_ENDPOINT = "api/crud/method/x";
}
```

Output will consist of ```*.js``` file:

```javascript
"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.ProjectConstants$MY_API_ENDPOINT = 'api/crud/method/x';
```

And corresponding ```*.d.ts``` file:
```typescript
export declare const ProjectConstants$MY_API_ENDPOINT: string;
```

Plugin will also be able to generate ``package.json`` file making it complete package that you can publish.

## Supported types
All java primitive types and their wrappers with addition of ``String`` are supported. 
Array versions of all those types are supported as well.

Additional type mappings can be defined using ``Mapper`` interface. Usage of this interface is described below.

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
    public static final Double THIS_NAME_REPEATS_IN_EVERY_SINGLE_CONSTANTS_CLASS = 1d; // Will generate: MyConstants$THIS_IS_NICER_NAME_ANYWAY = 1
}
```

### Annotation constants
Sometimes constants are used in class annotations and while you could create plain constant and like
shown above and supply this to annotations it can be tedious. This library provides shorthand for such
usage generating constants with following convention ``MyClass$$AnnotationName``:
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
By default, project will scan only for fields annotated by ``@TypescriptConstant``. See settings.

## Maven plugin
Library provides maven plugin for code generation. Simple usage:

```xml
<plugin>
    <groupId>dev.mtomczyk</groupId>
    <artifactId>ts-constants-generator-maven</artifactId>
    <version>0.0.5</version>
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
        <!-- Supply paths to packages that will be scanned for constants -->
        <paths>
            <path>dev.mtomczyk.example</path>
        </paths>
        <!-- Add if you wish plugin to spit some additional data about what it is doing -->
        <verbose>true</verbose>
        <!-- Keeping this on true will make sure only annotated constants will be scanned for generation -->
        <annotationMode>true</annotationMode>
        <!-- This will be base name for generated files -->
        <!-- In this case two files, constants.d.ts and constants.js will be created -->
        <targetFileName>constants</targetFileName>
        <!-- Whenever this plugin will be used alone keep this on true -->
        <!-- If used together with projects like typescript-generator you can disable package.json creation -->
        <standaloneMode>true</standaloneMode>
        <!-- Output path - where files will be saved -->
        <targetPath>ts</targetPath>
    </configuration>
</plugin>
```

## Mappers
Sometimes there is need to create constant from class that is not natively supported. In that case you can provide
appropriate mapper. Let's assume this simple currency class:

```Java
public class SimpleCurrency {
    private final int whole;
    private final int decimal;

    /* Constructors and getters for fields are assumed */
}
```

We can define mapper for it like that:

```Java
// We need to implement mapper interface and supply class that we want to map
public class SimpleCurrencyMapper implements Mapper<SimpleCurrency> {
    // Supply .class of class being mapped
    @Override
    public Class<SimpleCurrency> getMappedType() {
        return SimpleCurrency.class;
    }

    // Define TS type that will be result
    @Override
    public TsType getTsType() {
        return TsType.NUMBER;
    }

    // Assemble TS code equivalent of mapped class.
    // Be careful here, there are no safeguards against malformed code
    @Override
    public String getValue(SimpleCurrency object) {
        return String.valueOf(object.getWhole()) + "." + String.valueOf(object.getDecimal());
    }
}
```

When using plugin you can provide paths to mapper classes via ``mappings`` property:

```xml
<configuration>
    <mappings>
        <mapping>com.example.mappings</mapping>
    </mappings>
</configuration>
```

## Footnote
Library is in development period. Project will use server. Additionally, for versions <1.0.0
any change in y (x.y.z) may contain breaking changes in public api (Classes and methods annotated by @PublicApi).

For now, you need to compile from sources by yourself - not available in maven central.