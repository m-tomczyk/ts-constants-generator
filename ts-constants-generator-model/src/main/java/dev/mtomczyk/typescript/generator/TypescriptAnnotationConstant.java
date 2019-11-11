package dev.mtomczyk.typescript.generator;

import java.lang.annotation.*;

/**
 * Use to mark class as typescript constant.
 * Applicable only if annotation mode is active. See documentation for more information.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@PublicApi
public @interface TypescriptAnnotationConstant {
    Class<? extends Annotation> value();

    String name() default "";

    String annotationField() default "value";
}
