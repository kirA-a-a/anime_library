package io.student.rcc.utils;

import com.github.javafaker.Faker;
import java.util.UUID;

public class RandomUserDataUtils {

  public static String getRandomUserName() {
    return new Faker().animal().name() + "_" + UUID.randomUUID();
  }

  public static String getRandomFirstName() {
    return new Faker().name().name();
  }

  public static String getRandomLastName() {
    return new Faker().name().lastName();
  }

}
