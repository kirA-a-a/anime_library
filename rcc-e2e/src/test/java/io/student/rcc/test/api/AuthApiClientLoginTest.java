package io.student.rcc.test.api;

import static com.codeborne.selenide.Selenide.localStorage;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.student.rcc.config.Config;
import io.student.rcc.jupiter.annotation.ApiLogin;
import io.student.rcc.jupiter.annotation.Token;
import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.jupiter.extension.ApiLoginExtension;
import io.student.rcc.jupiter.extension.BrowserExtension;
import io.student.rcc.jupiter.extension.CookiesExtension;
import io.student.rcc.jupiter.extension.UserExtension;
import io.student.rcc.model.UserJson;
import io.student.rcc.page.MainPage;
import io.student.rcc.service.AuthApiClient;
import io.student.rcc.service.UsersDbClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;

@ExtendWith({
    CookiesExtension.class,
    UserExtension.class
})
public class AuthApiClientLoginTest {

  private static final Config CFG = Config.getInstance();
  private static final String PASSWORD = "12345";
  private static final String CRED_USERNAME = "api_login_cred_user";
  private static final String TITLE = "Ваши любимые картины и художники всегда рядом";

  private final AuthApiClient authApiClient = new AuthApiClient();

  @RegisterExtension
  private final BrowserExtension browserExtension = new BrowserExtension();

  @RegisterExtension
  private static final ApiLoginExtension apiLoginExtension = new ApiLoginExtension();

  @BeforeAll
  static void createUserForApiLoginCredentials() {
    UsersDbClient usersClient = new UsersDbClient();
    if (usersClient.findByUsername(CRED_USERNAME).isEmpty()) {
      usersClient.createUser(CRED_USERNAME, PASSWORD);
    }
  }

  @Test
  @User
  void shouldReturnIdToken(UserJson user) throws Exception {
    String idToken = authApiClient.login(user.username(), PASSWORD);
    assertNotNull(idToken);
  }

  @Test
  @User
  void shouldUiTests(UserJson user) throws Exception {
    String idToken = authApiClient.login(user.username(), PASSWORD);

    open(CFG.frontUrl());
    localStorage().setItem("id_token", idToken);
    open(CFG.frontUrl());

    new MainPage()
        .checkTitle(TITLE)
        .clickInProfile()
        .checkUserTag("@" + user.username());
  }

  @Test
  @User
  @ApiLogin
  void shouldLoginWithEmptyApiLoginAndUser(@Token String idToken, UserJson user) {
    assertNotNull(idToken);
    assertNotNull(user);
    assertNotNull(user.username());
  }

  @Test
  @ApiLogin(username = CRED_USERNAME, password = PASSWORD)
  void shouldLoginWithCredentialsInApiLoginWithoutUser(@Token String idToken) {
    assertNotNull(idToken);
  }
}
