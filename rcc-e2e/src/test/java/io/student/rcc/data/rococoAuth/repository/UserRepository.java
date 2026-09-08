package io.student.rcc.data.rococoAuth.repository;

import io.student.rcc.data.rococoAuth.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

  UserEntity create(UserEntity entity);

  Optional<UserEntity> findById(UUID id);

  List<UserEntity> findAll();

  Optional<UserEntity> findByUsername(String username);

  void delete(UserEntity entity);

}
