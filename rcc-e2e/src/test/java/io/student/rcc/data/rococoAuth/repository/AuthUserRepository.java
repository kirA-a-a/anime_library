package io.student.rcc.data.rococoAuth.repository;

import io.student.rcc.data.rococoAuth.entity.AuthUserEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthUserRepository {

  AuthUserEntity create(AuthUserEntity user);

  Optional<AuthUserEntity> findById(UUID id);

  Optional<AuthUserEntity> findByUsername(String username);

  List<AuthUserEntity> findAll();

  AuthUserEntity update(AuthUserEntity user);

  void delete(AuthUserEntity user);
}
