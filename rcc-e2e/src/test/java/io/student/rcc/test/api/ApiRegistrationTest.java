package io.student.rcc.test.api;

import static io.student.rcc.utils.RandomUserDataUtils.randomUserName;

import io.student.rcc.api.AuthApiClient;
import io.student.rcc.service.UsersApiClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ApiRegistrationTest {

  @Test
  @DisplayName("User registers and logs in using API")
  void apiRegistrationTest() {
    UsersApiClient client = new UsersApiClient();
    client.createUser(randomUserName(), "12345");
  }
}
