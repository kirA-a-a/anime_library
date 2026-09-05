package io.student.rcc.data.dao.rococoAuth.impl;

import io.student.rcc.config.Config;
import io.student.rcc.data.dao.rococoAuth.UserDao;
import io.student.rcc.data.dao.rococoAuth.entity.UserEntity;
import io.student.rcc.data.dao.rococoAuth.mapper.UserEntityRowMapper;
import io.student.rcc.data.tpl.DataSources;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserSpringJdbcDao implements UserDao {

  private static final Config CFG = Config.getInstance();

  @Override
  public UserEntity create(UserEntity entity) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.rccApiJdbcUrl()));

    if (entity.getId() == null) {
      entity.setId(UUID.randomUUID());
    }

    jdbcTemplate.update(
        "INSERT INTO `user`(id, username, firstname, lastname, avatar) "
            + "VALUES (UUID_TO_BIN(?, true), ?, ?, ?, ?)",
        entity.getId().toString(),
        entity.getUsername(),
        entity.getFirstname(),
        entity.getLastname(),
        entity.getAvatar() != null ? entity.getAvatar().getBytes() : null
    );

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
