package io.student.rcc.service;

import static io.student.rcc.data.Databases.xaTransaction;

import io.student.rcc.config.Config;
import io.student.rcc.data.Databases.XaConsumer;
import io.student.rcc.data.Databases.XaFunction;
import io.student.rcc.data.dao.rococoAuth.AuthAuthorityJdbcDao;
import io.student.rcc.data.dao.rococoAuth.AuthUserJdbcDao;
import io.student.rcc.data.dao.rococoAuth.UserJdbcDao;
import io.student.rcc.data.dao.rococoAuth.entity.AuthAuthorityEntity;
import io.student.rcc.data.dao.rococoAuth.entity.UserEntity;
import io.student.rcc.model.UserJson;
import java.sql.Connection;
import java.util.UUID;

public class UsersDbClient implements UsersClient {

  private final static Config CFG = Config.getInstance();

  @Override
  public UUID createUser(String username, String password) {
    return createUser(null, username, password);
  }

  public UUID createUser(String authority, String username, String password) {
    UUID authUserId = UUID.randomUUID();

    return xaTransaction(
        Connection.TRANSACTION_READ_COMMITTED,
        new XaFunction<>(connection -> {
          new AuthUserJdbcDao(connection).createUser(authUserId, username, password);

          if (authority != null) {
            AuthAuthorityEntity authorityEntity = new AuthAuthorityEntity();
            authorityEntity.setUserId(authUserId);
            authorityEntity.setAuthority(authority);
            new AuthAuthorityJdbcDao(connection).create(authorityEntity);
          }
          return authUserId;
        }, CFG.authJdbcUrl()),
        new XaFunction<>(connection -> {
          UserEntity entity = new UserEntity();
          entity.setId(authUserId);
          entity.setUsername(username);
          new UserJdbcDao(connection).create(entity);
          return authUserId;
        }, CFG.rccApiJdbcUrl())
    );
  }

  @Override
  public void delete(UserJson user) {
    xaTransaction(
        Connection.TRANSACTION_READ_COMMITTED,
        new XaConsumer(connection -> {
          new AuthAuthorityJdbcDao(connection).deleteByUserId(user.id());
          new AuthUserJdbcDao(connection).delete(user.id());
        }, CFG.authJdbcUrl()),
        new XaConsumer(connection -> {
          new UserJdbcDao(connection).delete(UserEntity.fromJson(user));
        }, CFG.rccApiJdbcUrl())
    );
  }
}
