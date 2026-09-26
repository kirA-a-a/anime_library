package io.student.rcc.service;

import io.student.rcc.data.rococoAuth.entity.Authority;
import io.student.rcc.model.UserJson;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface UsersClient {

  @Nonnull
  UserJson create(UserJson user);

  @Nonnull
  Optional<UserJson> findById(UUID id);

  @Nonnull
  Optional<UserJson> findByUsername(String username);

  @Nonnull
  List<UserJson> findAll();

  @Nonnull
  UserJson update(UserJson user);

  void delete(UserJson user);

  @Nonnull
  UUID createUser(String username, String password);

  @Nonnull
  UUID createUser(String username, String password, @Nullable Authority... authorities);
}
