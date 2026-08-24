package io.student.rcc.data.dao.rococoAuth;

import io.student.rcc.data.dao.rococoAuth.entity.UserEntity;
import java.util.Optional;
import java.util.UUID;

public interface UserDao {

  UserEntity create(UserEntity entity);

  Optional<UserEntity> findById(UUID id);

  Optional<UserEntity> findByUsername(String username);

  void delete(UserEntity entity);

}
