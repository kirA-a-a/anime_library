package io.student.rcc.test.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.student.rcc.api.UserApiClient;
import io.student.rcc.jupiter.annotation.ApiLogin;
import io.student.rcc.jupiter.annotation.Token;
import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.jupiter.annotation.meta.RestTest;
import io.student.rcc.jupiter.extension.ApiLoginExtension;
import io.student.rcc.model.UserJson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

@RestTest
public class RestTestTests {

  @RegisterExtension
  private static final ApiLoginExtension apiLoginExtension = ApiLoginExtension.rest();

  private final UserApiClient userApiClient = new UserApiClient();

  @User
  @Test
  @ApiLogin
  void shouldLoginWithEmptyApiLoginAndUser(@Token String token, UserJson user) {
    UserJson currentUser = userApiClient.getUser(token);

    assertNotNull(currentUser.id());
    assertEquals(user.username(), currentUser.username());
  }

}
