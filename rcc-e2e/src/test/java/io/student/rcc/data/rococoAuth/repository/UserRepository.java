package io.student.rcc.data.rococoAuth.repository;

import io.student.rcc.data.rococoAuth.entity.UserEntity;
import io.student.rcc.data.rococoAuth.repository.iml.UserRepositoryHibernate;
import io.student.rcc.data.rococoAuth.repository.iml.UserRepositoryJdbc;
import io.student.rcc.data.rococoAuth.repository.iml.UserRepositorySpringJdbc;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface UserRepository {

  static @Nonnull UserRepository getInstance() {
    return switch (System.getProperty("repository.impl", "jpa")) {
      case "jdbc" -> new UserRepositoryJdbc();
      case "spring-jdbc" -> new UserRepositorySpringJdbc();
      default -> new UserRepositoryHibernate();
    };
  }

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
