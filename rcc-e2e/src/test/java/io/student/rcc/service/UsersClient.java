package io.student.rcc.service;

import java.util.UUID;

import io.student.rcc.model.UserJson;

public interface UsersClient {

  UUID createUser(String username, String password);

  void delete(UserJson user);
}
