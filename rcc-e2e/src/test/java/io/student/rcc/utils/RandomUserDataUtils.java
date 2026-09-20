package io.student.rcc.utils;

import com.github.javafaker.Faker;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class RandomUserDataUtils {

  private static final Faker FAKER = new Faker();

  public static @Nonnull String randomUserName() {
    return FAKER.animal().name() + "_" + UUID.randomUUID();
  }

  public static @Nonnull String randomName() {
    return FAKER.name().name();
  }

  public static @Nonnull String randomLastName() {
    return FAKER.name().lastName();
  }

  public static @Nonnull String randomArtistName() {
    return FAKER.artist().name() + "_" + UUID.randomUUID().toString().substring(0, 8);
  }

  public static @Nonnull String randomPaintingTitle() {
    return FAKER.book().title();
  }

  public static @Nonnull String randomMuseumTitle() {
    return FAKER.university().name() + "_" + UUID.randomUUID().toString().substring(0, 8);
  }

  public static @Nonnull String randomCity() {
    return FAKER.address().city();
  }

  public static @Nonnull String randomSentence(int wordsCount) {
    return FAKER.lorem().sentence(wordsCount);
  }

}
