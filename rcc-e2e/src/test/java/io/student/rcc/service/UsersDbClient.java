package io.student.rcc.service;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoAuth.AuthAuthorityDao;
import io.student.rcc.data.rococoAuth.AuthUserDao;
import io.student.rcc.data.rococoAuth.UserDao;
import io.student.rcc.data.rococoAuth.entity.AuthAuthorityEntity;
import io.student.rcc.data.rococoAuth.entity.AuthAuthorityEntity.Authority;
import io.student.rcc.data.rococoAuth.entity.AuthUserEntity;
import io.student.rcc.data.rococoAuth.entity.UserEntity;
import io.student.rcc.data.rococoAuth.impl.AuthAuthorityDaoSpringJdbc;
import io.student.rcc.data.rococoAuth.impl.AuthUserDaoSpringJdbc;
import io.student.rcc.data.rococoAuth.impl.UserSpringJdbcDao;
import io.student.rcc.data.tpl.DataSources;
import io.student.rcc.model.UserJson;
import java.util.UUID;
import javax.sql.DataSource;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

public class UsersDbClient implements UsersClient {

  private final static Config CFG = Config.getInstance();
  private final AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
  private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoSpringJdbc();
  private final UserDao userDao = new UserSpringJdbcDao();

  private final DataSource authDs = DataSources.dataSource(CFG.authJdbcUrl());
  private final DataSource apiDs = DataSources.dataSource(CFG.rccApiJdbcUrl());

  private final TransactionTemplate txTemplate = new TransactionTemplate(
      new ChainedTransactionManager(
          new JdbcTransactionManager(authDs),
          new JdbcTransactionManager(apiDs)
      )
  );

  @Override
  public UUID createUser(String username, String password) {
    return createUser(username, password, new Authority[0]);
  }

  public UUID createUser(String username, String password, Authority... authorities) {
    return txTemplate.execute(status -> {

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

      UserEntity userEntity = new UserEntity();
      userEntity.setUsername(username);
      userDao.create(userEntity);

      return createdAuthUser.getId();
    });
  }

  @Override
  public void delete(UserJson user) {
    txTemplate.execute(
        status -> {
          authAuthorityDao.deleteByUserId(user.id());
          authUserDao.delete(user.id());

          userDao.findByUsername(user.username()).ifPresent(userDao::delete);

          return null;
        }
    );
  }
}
