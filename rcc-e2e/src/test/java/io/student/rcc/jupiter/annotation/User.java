package io.student.rcc.jupiter.annotation;

import io.student.rcc.jupiter.extension.ArtistExtension;
import io.student.rcc.jupiter.extension.CountryExtension;
import io.student.rcc.jupiter.extension.MuseumExtension;
import io.student.rcc.jupiter.extension.PaintingExtension;
import io.student.rcc.jupiter.extension.UserExtension;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith({
    UserExtension.class,
    CountryExtension.class,
    ArtistExtension.class,
    MuseumExtension.class,
    PaintingExtension.class
})
public @interface User {
  String username() default "";
  Artist[] artists() default {};
  Country[] countries() default {};
  Museum[] museums() default {};
  Painting[] paintings() default {};
}
