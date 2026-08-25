package io.student.rcc.data.dao.rococoAuth.impl;

import io.student.rcc.data.dao.rococoAuth.AuthUserDao;
import java.util.UUID;

public class AuthUserDaoSpringJdbc implements AuthUserDao {

  @Override
  public UUID createUser(UUID id, String username, String password) {
    return null;
  }

  @Override
  public void delete(UUID id) {

  }
}
