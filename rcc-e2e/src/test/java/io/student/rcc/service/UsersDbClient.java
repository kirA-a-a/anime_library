package io.student.rcc.service;

import io.student.rcc.config.Config;
import io.student.rcc.data.dao.rococoAuth.entity.AuthAuthorityEntity;
import io.student.rcc.data.dao.rococoAuth.entity.AuthAuthorityEntity.Authority;
import io.student.rcc.data.dao.rococoAuth.entity.AuthUserEntity;
import io.student.rcc.data.dao.rococoAuth.entity.UserEntity;
import io.student.rcc.data.dao.rococoAuth.impl.AuthAuthorityJdbcDao;
import io.student.rcc.data.dao.rococoAuth.impl.AuthUserJdbcDao;
import io.student.rcc.data.dao.rococoAuth.impl.UserJdbcDao;
import io.student.rcc.data.tpl.JdbcTransactionTemplate;
import io.student.rcc.data.tpl.XaTransactionTemplate;
import io.student.rcc.model.UserJson;
import java.util.UUID;

public class UsersDbClient implements UsersClient {

  private final static Config CFG = Config.getInstance();

  private final AuthUserJdbcDao authUserDao = new AuthUserJdbcDao();
  private final AuthAuthorityJdbcDao authAuthorityDao = new AuthAuthorityJdbcDao();
  private final UserJdbcDao userDao = new UserJdbcDao();

  private final JdbcTransactionTemplate txTemplate = new JdbcTransactionTemplate(
      CFG.authJdbcUrl()
  );

  private final XaTransactionTemplate xaTemplate = new XaTransactionTemplate(
      CFG.authJdbcUrl(),
      CFG.rccApiJdbcUrl()
  );

  @Override
  public UUID createUser(String username, String password) {
    return createUser(username, password, new Authority[0]);
  }

  public UUID createUser(String username, String password, Authority... authorities) {
    UUID authUserId = txTemplate.execute(() -> {
      AuthUserEntity authUser = new AuthUserEntity();
      authUser.setUsername(username);
      authUser.setPassword(password);
      authUser.setEnabled(true);
      authUser.setAccountNonExpired(true);
      authUser.setAccountNonLocked(true);
      authUser.setCredentialsNonExpired(true);

      AuthUserEntity createdAuthUser = authUserDao.create(authUser);

      if (authorities != null && authorities.length > 0) {
        AuthAuthorityEntity[] entities = new AuthAuthorityEntity[authorities.length];
        for (int i = 0; i < authorities.length; i++) {
          AuthAuthorityEntity authorityEntity = new AuthAuthorityEntity();
          authorityEntity.setUserId(createdAuthUser.getId());
          authorityEntity.setAuthority(authorities[i]);
          entities[i] = authorityEntity;
        }
        authAuthorityDao.create(entities);
      }

      return createdAuthUser.getId();
    });

    UserEntity userEntity = new UserEntity();
    userEntity.setUsername(username);
    userDao.create(userEntity);

    return authUserId;
  }

  @Override
  public void delete(UserJson user) {
    xaTemplate.execute(
        () -> {
          authAuthorityDao.deleteByUserId(user.id());
          authUserDao.delete(user.id());
          return null;
        },
        () -> {
          userDao.findByUsername(user.username()).ifPresent(userDao::delete);
          return null;
        }
    );
  }
}
