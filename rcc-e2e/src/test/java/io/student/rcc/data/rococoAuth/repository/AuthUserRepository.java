package io.student.rcc.data.rococoAuth.repository;

import io.student.rcc.data.rococoAuth.entity.AuthUserEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthUserRepository {

  AuthUserEntity create(AuthUserEntity user);

  Optional<AuthUserEntity> findById(UUID id);

  List<AuthUserEntity> findAll();

  void delete(UUID id);
}
