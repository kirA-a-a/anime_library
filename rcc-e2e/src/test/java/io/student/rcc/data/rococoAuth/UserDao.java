package io.student.rcc.data.rococoAuth;

import io.student.rcc.data.rococoAuth.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface UserDao {

  @Nonnull
  UserEntity create(UserEntity entity);

  @Nonnull
  Optional<UserEntity> findById(UUID id);

  @Nonnull
  Optional<UserEntity> findByUsername(String username);

  @Nonnull
  List<UserEntity> findAll();

  @Nonnull
  UserEntity update(UserEntity entity);

  void delete(UserEntity entity);
}
