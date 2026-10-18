package io.student.rcc.data.rococoAuth.repository.iml;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoAuth.entity.AuthUserEntity;
import io.student.rcc.data.rococoAuth.mapper.AuthUserWithAuthoritiesExtractor;
import io.student.rcc.data.rococoAuth.repository.AuthUserRepository;
import io.student.rcc.data.tpl.DataSources;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthUserRepositorySpringJdbc implements AuthUserRepository {

  private static final Config CFG = Config.getInstance();
  private final PasswordEncoder passwordEncoder =
      PasswordEncoderFactories.createDelegatingPasswordEncoder();

  private static final String USER_WITH_AUTHORITIES_SELECT =
      "SELECT BIN_TO_UUID(u.id, true) AS id, u.username, u.password, u.enabled, "
          + "u.account_non_expired, u.account_non_locked, u.credentials_non_expired, "
          + "BIN_TO_UUID(a.id, true) AS authority_id, "
          + "BIN_TO_UUID(a.user_id, true) AS user_id, "
          + "a.authority AS authority "
          + "FROM `user` u "
          + "LEFT JOIN `authority` a ON u.id = a.user_id";

  private JdbcTemplate jdbcTemplate() {
    return new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
  }

  @Override
  public AuthUserEntity create(AuthUserEntity user) {
    if (user.getId() == null) {
      user.setId(UUID.randomUUID());
    }
    jdbcTemplate().update(
        "INSERT INTO `user` "
            + "(id, username, password, enabled, account_non_expired, account_non_locked, "
            + "credentials_non_expired) "
            + "VALUES (UUID_TO_BIN(?, true), ?, ?, ?, ?, ?, ?)",
        user.getId().toString(),
        user.getUsername(),
        passwordEncoder.encode(user.getPassword()),
        user.isEnabled(),
        user.isAccountNonExpired(),
        user.isAccountNonLocked(),
        user.isCredentialsNonExpired()
    );
    return user;
  }

  @Override
  public Optional<AuthUserEntity> findById(UUID id) {
    List<AuthUserEntity> result = jdbcTemplate().query(
        USER_WITH_AUTHORITIES_SELECT + " WHERE u.id = UUID_TO_BIN(?, true)",
        AuthUserWithAuthoritiesExtractor.instance,
        id.toString()
    );
    return result.stream().findFirst();
  }

  @Override
  public Optional<AuthUserEntity> findByUsername(String username) {
    List<AuthUserEntity> result = jdbcTemplate().query(
        USER_WITH_AUTHORITIES_SELECT + " WHERE u.username = ?",
        AuthUserWithAuthoritiesExtractor.instance,
        username
    );
    return result.stream().findFirst();
  }

  @Override
  public List<AuthUserEntity> findAll() {
    return jdbcTemplate()
        .query(USER_WITH_AUTHORITIES_SELECT, AuthUserWithAuthoritiesExtractor.instance);
  }

  @Override
  public AuthUserEntity update(AuthUserEntity user) {
    jdbcTemplate().update(
        "UPDATE `user` SET username = ?, password = ?, enabled = ?, "
            + "account_non_expired = ?, account_non_locked = ?, credentials_non_expired = ? "
            + "WHERE id = UUID_TO_BIN(?, true)",
        user.getUsername(),
        passwordEncoder.encode(user.getPassword()),
        user.isEnabled(),
        user.isAccountNonExpired(),
        user.isAccountNonLocked(),
        user.isCredentialsNonExpired(),
        user.getId().toString()
    );
    return user;
  }

  @Override
  public void delete(AuthUserEntity user) {
    jdbcTemplate().update(
        "DELETE FROM `authority` WHERE user_id = UUID_TO_BIN(?, true)",
        user.getId().toString()
    );
    jdbcTemplate().update(
        "DELETE FROM `user` WHERE id = UUID_TO_BIN(?, true)",
        user.getId().toString()
    );
  }
}
