package io.student.rcc.jupiter.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface Museum {

  String title() default "";

  String description() default "";

  String city() default "";

  String content() default "";

  String country() default "";
}
