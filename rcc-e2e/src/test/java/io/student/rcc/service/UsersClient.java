package io.student.rcc.service;

import io.student.rcc.model.UserJson;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsersClient {

  UserJson create(UserJson user);

  Optional<UserJson> findById(UUID id);

  Optional<UserJson> findByUsername(String username);

  List<UserJson> findAll();

  UserJson update(UserJson user);

  void delete(UserJson user);

  UUID createUser(String username, String password);
}
