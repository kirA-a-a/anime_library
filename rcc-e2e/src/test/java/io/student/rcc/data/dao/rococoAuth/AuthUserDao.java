package io.student.rcc.data.dao.rococoAuth;

import java.util.UUID;

public interface AuthUserDao {

  UUID createUser(UUID id, String username, String password);

  void delete(UUID id);
}
