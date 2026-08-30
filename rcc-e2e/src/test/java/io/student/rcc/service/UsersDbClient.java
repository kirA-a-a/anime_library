package io.student.rcc.service;

import static io.student.rcc.data.Databases.xaTransaction;

import io.student.rcc.config.Config;
import io.student.rcc.data.Databases.XaConsumer;
import io.student.rcc.data.Databases.XaFunction;
import io.student.rcc.data.dao.rococoAuth.impl.AuthAuthorityJdbcDao;
import io.student.rcc.data.dao.rococoAuth.impl.AuthUserJdbcDao;
import io.student.rcc.data.dao.rococoAuth.impl.UserJdbcDao;
import io.student.rcc.data.dao.rococoAuth.entity.AuthAuthorityEntity;
import io.student.rcc.data.dao.rococoAuth.entity.AuthAuthorityEntity.Authority;
import io.student.rcc.data.dao.rococoAuth.entity.AuthUserEntity;
import io.student.rcc.data.dao.rococoAuth.entity.UserEntity;
import io.student.rcc.model.UserJson;
import java.sql.Connection;
import java.util.UUID;

public class UsersDbClient implements UsersClient {

  private final static Config CFG = Config.getInstance();

  @Override
  public UUID createUser(String username, String password) {
    return createUser(username, password, new Authority[0]);
  }

  public UUID createUser(String username, String password, Authority... authorities) {
    UUID authUserId = UUID.randomUUID();

    return xaTransaction(
        Connection.TRANSACTION_READ_COMMITTED,
        new XaFunction<>(connection -> {
          AuthUserEntity authUser = new AuthUserEntity();
          authUser.setId(authUserId);
          authUser.setUsername(username);
          authUser.setPassword(password);
          authUser.setEnabled(true);
          authUser.setAccountNonExpired(true);
          authUser.setAccountNonLocked(true);
          authUser.setCredentialsNonExpired(true);
          new AuthUserJdbcDao(connection).create(authUser);

          if (authorities != null && authorities.length > 0) {
            AuthAuthorityEntity[] entities = new AuthAuthorityEntity[authorities.length];
            for (int i = 0; i < authorities.length; i++) {
              AuthAuthorityEntity authorityEntity = new AuthAuthorityEntity();
              authorityEntity.setUserId(authUserId);
              authorityEntity.setAuthority(authorities[i]);
              entities[i] = authorityEntity;
            }
            new AuthAuthorityJdbcDao(connection).create(entities);
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
