package io.student.rcc.jupiter.annotation.meta;

import io.qameta.allure.junit5.AllureJunit5;
import io.student.rcc.jupiter.extension.UsersQueueExtension;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith({
    AllureJunit5.class,
    UsersQueueExtension.class,
})
public @interface WebTest {

}
