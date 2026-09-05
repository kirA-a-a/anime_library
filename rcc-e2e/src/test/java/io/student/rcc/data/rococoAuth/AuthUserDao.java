package io.student.rcc.data.rococoAuth;

import io.student.rcc.data.rococoAuth.entity.AuthUserEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthUserDao {

  AuthUserEntity create(AuthUserEntity user);

  Optional<AuthUserEntity> findById(UUID id);

  List<AuthUserEntity> findAll();

  void delete(UUID id);
}
