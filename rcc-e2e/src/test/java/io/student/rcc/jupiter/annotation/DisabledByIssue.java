package io.student.rcc.jupiter.annotation;

import io.student.rcc.jupiter.extension.IssueExtension;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(IssueExtension.class)
public @interface DisabledByIssue {
  String value();
}
