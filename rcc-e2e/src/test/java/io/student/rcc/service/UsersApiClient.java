package io.student.rcc.service;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

import io.student.rcc.api.BaseRestClient;
import io.student.rcc.api.UserApiClient;
import io.student.rcc.data.rococoAuth.entity.Authority;
import io.student.rcc.jupiter.extension.ApiLoginExtension;
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

  private final io.student.rcc.api.AuthApiClient authApiClient;
  private final AuthApiClient authLoginClient;
  private final UserApiClient userApiClient;

  public UsersApiClient() {
    super(CFG.apiUrl());
    this.authApiClient = new io.student.rcc.api.AuthApiClient();
    this.authLoginClient = new AuthApiClient();
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

  @Override
  public @NonNull UUID createUser(
      String username,
      String password
  ) {
    authApiClient.registerUser(username, password);
    authLoginClient.login(username, password);

    AtomicReference<UserJson> currentUser = new AtomicReference<>();

    await()
        .atMost(5, SECONDS)
        .pollInterval(100, MILLISECONDS)
        .until(() -> {
          UserJson userJson = userApiClient.getUser(ApiLoginExtension.getToken());
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
