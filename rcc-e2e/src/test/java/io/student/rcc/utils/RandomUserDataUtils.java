package io.student.rcc.utils;

import com.github.javafaker.Faker;
import java.util.UUID;

public class RandomUserDataUtils {

  private static final Faker faker = new Faker();

  public static String getRandomUserName() {
    return faker.animal().name() + "_" + UUID.randomUUID();
  }

  public static String getRandomFirstName() {
    return faker.name().name();
  }

  public static String getRandomLastName() {
    return faker.name().lastName();
  }

}
