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
exports.MY_API_ENDPOINT = 'api/crud/method/x';
```

And corresponding *.d.ts file:
```typescript
export declare const MY_API_ENDPOINT: string;
```

## Supported types
All java primitive types and their wrappers with addition of ``String`` are supported.

Additional type mappings can be defined using ``Mapper`` interface

## TypescriptConstant annotation
By default project will scan only for fields annotated by @TypescriptConstant

## Maven plugin
Library provides maven plugin for code generation. Simple usage:

```xml
<plugin>
    <groupId>dev.mtomczyk</groupId>
    <artifactId>ts-constants-generator-maven</artifactId>
    <version>0.0.3-SNAPSHOT</version>
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
            <path>com.example</path>
        </paths>
    </configuration>
</plugin>
```

## Footnote
Library is in development period. Project will use semver. Additionally for versions <1.0.0
any change in y (x.y.z) may contain breaking changes in public api (Classes and methods annotated by @PublicApi).

For now you need to compile from sources by yourself - not available in maven central.