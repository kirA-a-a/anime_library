package io.student.rcc.data.rococoAuth.impl;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoAuth.AuthUserDao;
import io.student.rcc.data.rococoAuth.entity.AuthUserEntity;
import io.student.rcc.data.rococoAuth.mapper.AuthUserEntityRowMapper;
import io.student.rcc.data.tpl.DataSources;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthUserDaoSpringJdbc implements AuthUserDao {

  private static final Config CFG = Config.getInstance();
  private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  @Override
  public AuthUserEntity create(AuthUserEntity user) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));

    if (user.getId() == null) {
      user.setId(UUID.randomUUID());
    }

    jdbcTemplate.update(
        "INSERT INTO `user` "
            + "(id, username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)"
            + " VALUES (UUID_TO_BIN(?, true), ?, ?, ?, ?, ?, ?)",
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
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));

    return Optional.ofNullable(
        jdbcTemplate
            .queryForObject(
                "SELECT * FROM `user` WHERE id = UUID_TO_BIN(?, true)",
                AuthUserEntityRowMapper.instance,
                id.toString()
            )
    );
  }

  @Override
  public List<AuthUserEntity> findAll() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));

    return jdbcTemplate.query(
        "SELECT BIN_TO_UUID(id, true) AS id, username, password, enabled, "
            + "account_non_expired, account_non_locked, credentials_non_expired "
            + "FROM `user`",
        AuthUserEntityRowMapper.instance
    );
  }

  @Override
  public void delete(UUID id) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));

    jdbcTemplate.update(
        "DELETE FROM `user` WHERE id = UUID_TO_BIN(?, true)",
        id.toString()
    );
  }
}
