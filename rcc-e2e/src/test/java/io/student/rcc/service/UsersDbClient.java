package io.student.rcc.service;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoAuth.entity.AuthAuthorityEntity;
import io.student.rcc.data.rococoAuth.entity.Authority;
import io.student.rcc.data.rococoAuth.entity.AuthUserEntity;
import io.student.rcc.data.rococoAuth.entity.UserEntity;
import io.student.rcc.data.rococoAuth.repository.AuthUserRepository;
import io.student.rcc.data.rococoAuth.repository.UserRepository;
import io.student.rcc.data.rococoAuth.repository.iml.AuthUserRepositoryJdbc;
import io.student.rcc.data.rococoAuth.repository.iml.UserRepositoryJdbc;
import io.student.rcc.data.tpl.DataSources;
import io.student.rcc.model.UserJson;
import java.util.UUID;
import javax.sql.DataSource;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

public class UsersDbClient implements UsersClient {

  private final static Config CFG = Config.getInstance();
  private final AuthUserRepository authUserRepository = new AuthUserRepositoryJdbc();
  private final UserRepository userRepository = new UserRepositoryJdbc();

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

      if (authorities != null && authorities.length > 0) {
        for (Authority authority : authorities) {
          AuthAuthorityEntity authorityEntity = new AuthAuthorityEntity();
          authorityEntity.setAuthority(authority);
          authUser.addAuthorities(authorityEntity);
        }
      }

      AuthUserEntity createdAuthUser = authUserRepository.create(authUser);

      UserEntity userEntity = new UserEntity();
      userEntity.setUsername(username);
      userRepository.create(userEntity);

      return createdAuthUser.getId();
    });
  }

  @Override
  public void delete(UserJson user) {
    txTemplate.execute(
        status -> {
          authUserRepository.delete(user.id());

          userRepository.findByUsername(user.username()).ifPresent(userRepository::delete);

          return null;
        }
    );
  }
}
