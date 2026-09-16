package io.student.rcc.data.rococoAuth;

import io.student.rcc.data.rococoAuth.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDao {

  UserEntity create(UserEntity entity);

  Optional<UserEntity> findById(UUID id);

  Optional<UserEntity> findByUsername(String username);

  List<UserEntity> findAll();

  UserEntity update(UserEntity entity);

  void delete(UserEntity entity);
}
