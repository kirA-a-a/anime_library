package io.student.rcc.service;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

import io.student.rcc.api.AuthApiClient;
import io.student.rcc.api.BaseRestClient;
import io.student.rcc.api.UserApiClient;
import io.student.rcc.data.rococoAuth.entity.Authority;
import io.student.rcc.model.UserJson;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.ParametersAreNonnullByDefault;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@Slf4j
@ParametersAreNonnullByDefault
public class UsersApiClient extends BaseRestClient implements UsersClient {

  private final AuthApiClient authApiClient;
  private final UserApiClient userApiClient;

  public UsersApiClient() {
    super(CFG.apiUrl());
    this.authApiClient = new AuthApiClient();
    this.userApiClient = new UserApiClient();
  }

  @Override
  public @NonNull UserJson create(UserJson user) {
    return null;
  }

  @Override
  public @NonNull Optional<UserJson> findById(UUID id) {
    return Optional.empty();
  }

  @Override
  public @NonNull Optional<UserJson> findByUsername(String username) {
    return Optional.empty();
  }

  @Override
  public @NonNull List<UserJson> findAll() {
    return List.of();
  }

  @Override
  public @NonNull UserJson update(UserJson user) {
    return null;
  }

  @Override
  public void delete(UserJson user) {

  }

  /**
   * TODO: сейчас createUser не доходит до реального id через API.
   * 1) userApiClient.getUser() бьёт в GET /api/user без Authorization → 401,
   *    await крутится до таймаута. Нужен JWT (OAuth code+PKCE / login flow),
   *    Bearer в UserApiClient (header или interceptor).
   * 2) даже с JWT UserController#getUser — заглушка (UUID.randomUUID(),
   *    хардкод имени), не userdata из БД и не стабильный id после register.
   * Пока для тестов надёжнее UsersDbClient; этот метод — каркас register → poll.
   */
  @Override
  public @NonNull UUID createUser(
      String username,
      String password
  ) {
    authApiClient.registerUser(username, password);

    AtomicReference<UserJson> currentUser = new AtomicReference<>();

    await()
        .atMost(5, SECONDS)
        .pollInterval(100, MILLISECONDS)
        .until(() -> {
          // TODO: getUser() пока всегда null — нет Bearer JWT → 401 на /api/user
          UserJson userJson = userApiClient.getUser();
          currentUser.set(userJson);
          return userJson != null && userJson.id() != null;
        });

    UUID id = Objects.requireNonNull(currentUser.get().id());
    return id;
  }

  @Override
  public @NonNull UUID createUser(String username, String password,
      @Nullable Authority... authorities) {
    return null;
  }
}
