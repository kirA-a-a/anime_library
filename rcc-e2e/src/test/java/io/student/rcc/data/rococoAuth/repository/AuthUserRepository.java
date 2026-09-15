package io.student.rcc.data.rococoAuth.repository;

import io.student.rcc.data.rococoAuth.entity.AuthUserEntity;
import io.student.rcc.data.rococoAuth.repository.iml.AuthUserRepositoryHibernate;
import io.student.rcc.data.rococoAuth.repository.iml.AuthUserRepositoryJdbc;
import io.student.rcc.data.rococoAuth.repository.iml.AuthUserRepositorySpringJdbc;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthUserRepository {

  static AuthUserRepository getInstance() {
    return switch (System.getProperty("repository.impl", "jpa")) {
      case "jdbc" -> new AuthUserRepositoryJdbc();
      case "spring-jdbc" -> new AuthUserRepositorySpringJdbc();
      default -> new AuthUserRepositoryHibernate();
    };
  }

  AuthUserEntity create(AuthUserEntity user);

  Optional<AuthUserEntity> findById(UUID id);

  Optional<AuthUserEntity> findByUsername(String username);

  List<AuthUserEntity> findAll();

  AuthUserEntity update(AuthUserEntity user);

  void delete(AuthUserEntity user);
}
