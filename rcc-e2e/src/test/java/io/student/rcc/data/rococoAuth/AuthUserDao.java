package io.student.rcc.data.rococoAuth;

import io.student.rcc.data.rococoAuth.entity.AuthUserEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface AuthUserDao {

  @Nonnull
  AuthUserEntity create(AuthUserEntity user);

  @Nonnull
  Optional<AuthUserEntity> findById(UUID id);

  @Nonnull
  Optional<AuthUserEntity> findByUsername(String username);

  @Nonnull
  List<AuthUserEntity> findAll();

  @Nonnull
  AuthUserEntity update(AuthUserEntity user);

  void delete(AuthUserEntity user);
}
