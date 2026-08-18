package io.student.rcc.data.dao;

import java.util.UUID;

public interface AuthUserDao {

  UUID createUser(UUID id, String username, String password);
}
