package io.student.rcc.service;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoAuth.entity.AuthAuthorityEntity;
import io.student.rcc.data.rococoAuth.entity.Authority;
import io.student.rcc.data.rococoAuth.entity.AuthUserEntity;
import io.student.rcc.data.rococoAuth.entity.UserEntity;
import io.student.rcc.data.rococoAuth.repository.AuthUserRepository;
import io.student.rcc.data.rococoAuth.repository.UserRepository;
import io.student.rcc.data.rococoAuth.repository.iml.AuthUserRepositoryHibernate;
import io.student.rcc.data.rococoAuth.repository.iml.UserRepositoryHibernate;
import io.student.rcc.data.tpl.XaTransactionTemplate;
import io.student.rcc.model.UserJson;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UsersDbClient implements UsersClient {

  private final static Config CFG = Config.getInstance();
  private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();
  private final UserRepository userRepository = new UserRepositoryHibernate();

  private final XaTransactionTemplate xaTemplate = new XaTransactionTemplate(
      CFG.authJdbcUrl(),
      CFG.rccApiJdbcUrl()
  );

  private static final String DEFAULT_PASSWORD = "12345";

  @Override
  public UserJson create(UserJson user) {
    UUID id = createUser(user.username(), DEFAULT_PASSWORD);
    return update(new UserJson(
        id,
        user.username(),
        user.firstname(),
        user.lastname(),
        user.avatar()
    ));
  }

  @Override
  public Optional<UserJson> findById(UUID id) {
    return xaTemplate.execute(() ->
        userRepository.findById(id).map(UserJson::fromEntity)
    );
  }

  @Override
  public Optional<UserJson> findByUsername(String username) {
    return xaTemplate.execute(() ->
        userRepository.findByUsername(username).map(UserJson::fromEntity)
    );
  }

  @Override
  public List<UserJson> findAll() {
    return xaTemplate.execute(() ->
        userRepository.findAll().stream().map(UserJson::fromEntity).toList()
    );
  }

  @Override
  public UserJson update(UserJson user) {
    return xaTemplate.execute(() -> {
      UserEntity entity = userRepository.findByUsername(user.username())
          .or(() -> userRepository.findById(user.id()))
          .orElseThrow(() -> new IllegalArgumentException(
              "User not found: " + user.username()
          ));
      entity.setUsername(user.username());
      entity.setFirstname(user.firstname());
      entity.setLastname(user.lastname());
      entity.setAvatar(user.avatar());
      return UserJson.fromEntity(userRepository.update(entity));
    });
  }

  @Override
  public UUID createUser(String username, String password) {
    return createUser(username, password, new Authority[0]);
  }

  public UUID createUser(String username, String password, Authority... authorities) {
    return xaTemplate.execute(() -> {

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
    xaTemplate.execute(() -> {
      AuthUserEntity authUser = new AuthUserEntity();
      authUser.setId(user.id());
      authUserRepository.delete(authUser);

      userRepository.delete(UserEntity.fromJson(user));
      return null;
    });
  }
}
