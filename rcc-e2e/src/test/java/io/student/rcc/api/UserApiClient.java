package io.student.rcc.api;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.qameta.allure.Step;
import io.student.rcc.model.UserJson;
import java.io.IOException;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import retrofit2.Response;

@ParametersAreNonnullByDefault
public class UserApiClient extends BaseRestClient {

  private final UserApi userApi;

  public UserApiClient() {
    super(CFG.apiUrl(), BODY);
    this.userApi = retrofit.create(UserApi.class);
  }

  @Step("GET /api/user")
  public @Nonnull UserJson getUser(String token) {
    final Response<UserJson> response;
    try {
      response = userApi.getUser(token).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return Objects.requireNonNull(response.body());
  }

  @Step("PATCH /api/user")
  public @Nonnull UserJson updateUser(String token, UserJson user) {
    final Response<UserJson> response;
    try {
      response = userApi.updateUser(token, user).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return Objects.requireNonNull(response.body());
  }
}
