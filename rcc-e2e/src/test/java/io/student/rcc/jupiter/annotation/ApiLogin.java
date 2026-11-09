package io.student.rcc.jupiter.annotation;

import io.student.rcc.jupiter.extension.ApiLoginExtension;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith({
    ApiLoginExtension.class
})
public @interface ApiLogin {

  String username() default "";

  String password() default "";
}
