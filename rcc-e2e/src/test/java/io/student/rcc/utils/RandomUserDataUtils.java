package io.student.rcc.utils;

import com.github.javafaker.Faker;
import java.util.UUID;

public class RandomUserDataUtils {

  public static String randomUserName() {
    return new Faker().animal().name() + "_" + UUID.randomUUID();
  }

  public static String randomName() {
    return new Faker().name().name();
  }

  public static String randomLastName() {
    return new Faker().name().lastName();
  }

  public static String randomArtistName() {
    return new Faker().artist().name() + "_" + UUID.randomUUID().toString().substring(0, 8);
  }

  public static String randomPaintingTitle() {
    return new Faker().book().title();
  }

  public static String randomMuseumTitle() {
    return new Faker().university().name() + "_" + UUID.randomUUID().toString().substring(0, 8);
  }

  public static String randomCity() {
    return new Faker().address().city();
  }

  public static String randomSentence(int wordsCount) {
    return new Faker().lorem().sentence(wordsCount);
  }

}
