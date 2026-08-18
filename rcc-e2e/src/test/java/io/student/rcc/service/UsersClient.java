package io.student.rcc.service;

import java.util.UUID;

public interface UsersClient {

  UUID createUser(String username, String password);
}
