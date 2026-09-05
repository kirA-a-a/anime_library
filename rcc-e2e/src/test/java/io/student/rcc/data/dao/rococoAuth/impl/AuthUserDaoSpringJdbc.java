package io.student.rcc.data.dao.rococoAuth.impl;

import io.student.rcc.config.Config;
import io.student.rcc.data.dao.rococoAuth.AuthUserDao;
import io.student.rcc.data.dao.rococoAuth.entity.AuthUserEntity;
import io.student.rcc.data.dao.rococoAuth.mapper.AuthUserEntityRowMapper;
import io.student.rcc.data.tpl.DataSources;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthUserDaoSpringJdbc implements AuthUserDao {

  private static final Config CFG = Config.getInstance();
  private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  @Override
  public AuthUserEntity create(AuthUserEntity user) {

    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(
        con -> {
          PreparedStatement ps = con.prepareStatement(
              "INSERT INTO `user` "
                  + "(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)"
                  + " VALUES (?, ?, ?, ?, ?, ?)",
              Statement.RETURN_GENERATED_KEYS
          );

          ps.setString(1, user.getUsername());
          ps.setString(2, passwordEncoder.encode(user.getPassword()));
          ps.setBoolean(3, user.isEnabled());
          ps.setBoolean(4, user.isAccountNonExpired());
          ps.setBoolean(5, user.isAccountNonLocked());
          ps.setBoolean(6, user.isCredentialsNonExpired());

          return ps;
        }, keyHolder
    );

    final UUID generatedKey = (UUID) keyHolder.getKeys().get("id");
    user.setId(generatedKey);

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
