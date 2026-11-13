package io.student.rcc.data.rococoAuth.repository;

import io.student.rcc.data.rococoAuth.entity.AuthUserEntity;
import io.student.rcc.data.rococoAuth.repository.iml.AuthUserRepositoryHibernate;
import io.student.rcc.data.rococoAuth.repository.iml.AuthUserRepositoryJdbc;
import io.student.rcc.data.rococoAuth.repository.iml.AuthUserRepositorySpringJdbc;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface AuthUserRepository {

  static @Nonnull AuthUserRepository getInstance() {
    return switch (System.getProperty("repository.impl", "jpa")) {
      case "jdbc" -> new AuthUserRepositoryJdbc();
      case "spring-jdbc" -> new AuthUserRepositorySpringJdbc();
      default -> new AuthUserRepositoryHibernate();
    };
  }

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
