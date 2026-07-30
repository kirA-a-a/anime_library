package io.student.rcc.data.dao;

import io.student.rcc.data.entity.UserEntity;
import java.util.Optional;
import java.util.UUID;

public interface UserDao {

  UserEntity create(UserEntity entity);

  Optional<UserEntity> findById(UUID id);

  Optional<UserEntity> findByUsername(String username);

  void delete(UserEntity entity);

}
