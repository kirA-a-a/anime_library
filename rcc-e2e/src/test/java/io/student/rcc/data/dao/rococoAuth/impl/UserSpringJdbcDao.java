package io.student.rcc.data.dao.rococoAuth.impl;

import io.student.rcc.config.Config;
import io.student.rcc.data.dao.rococoAuth.UserDao;
import io.student.rcc.data.dao.rococoAuth.entity.UserEntity;
import io.student.rcc.data.dao.rococoAuth.mapper.UserEntityRowMapper;
import io.student.rcc.data.tpl.DataSources;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class UserSpringJdbcDao implements UserDao {

  private static final Config CFG = Config.getInstance();

  @Override
  public UserEntity create(UserEntity entity) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.rccApiJdbcUrl()));
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(
        con -> {

          PreparedStatement ps = con.prepareStatement(
              "INSERT INTO `user`(username, firstname, lastname, avatar) "
                  + "VALUES (?, ?, ?, ?)",
              Statement.RETURN_GENERATED_KEYS
          );

          ps.setString(1, entity.getUsername());
          ps.setString(2, entity.getFirstname());
          ps.setString(3, entity.getLastname());
          ps.setBytes(4, entity.getAvatar() != null ? entity.getAvatar().getBytes() : null);

          return ps;

        }, keyHolder
    );

    final UUID generatedKey = (UUID) keyHolder.getKeys().get("id");
    entity.setId(generatedKey);

    return entity;
  }

  @Override
  public Optional<UserEntity> findById(UUID id) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.rccApiJdbcUrl()));

    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            "SELECT BIN_TO_UUID(id, true) as id, username, firstname, lastname, avatar "
                + "FROM `user` WHERE id = UUID_TO_BIN(?, true)",
            UserEntityRowMapper.instance,
            id.toString()
        )
    );
  }

  @Override
  public List<UserEntity> findAll() {

    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.rccApiJdbcUrl()));

    return jdbcTemplate.query(
        "SELECT BIN_TO_UUID(id, true) AS id, username, firstname, lastname, avatar "
            + "FROM `user`",
        UserEntityRowMapper.instance
    );
  }

  @Override
  public Optional<UserEntity> findByUsername(String username) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.rccApiJdbcUrl()));

    UserEntity user = jdbcTemplate.queryForObject(
        "SELECT BIN_TO_UUID(id, true) as id, username, firstname, lastname, avatar "
            + "FROM `user` WHERE username = ?",
        UserEntityRowMapper.instance,
        username
    );

    return Optional.ofNullable(user);
  }

  @Override
  public void delete(UserEntity entity) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.rccApiJdbcUrl()));

    jdbcTemplate.update(
        "DELETE FROM `user` WHERE id = UUID_TO_BIN(?, true)",
        entity.getId().toString()
    );
  }
}
