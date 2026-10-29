package io.student.rcc.data.rococoAuth.repository.iml;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoAuth.entity.UserEntity;
import io.student.rcc.data.rococoAuth.mapper.UserEntityRowMapper;
import io.student.rcc.data.rococoAuth.repository.UserRepository;
import io.student.rcc.data.tpl.DataSources;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserRepositorySpringJdbc implements UserRepository {

  private static final Config CFG = Config.getInstance();

  private JdbcTemplate jdbcTemplate() {
    return new JdbcTemplate(DataSources.dataSource(CFG.rccApiJdbcUrl()));
  }

  @Override
  public UserEntity create(UserEntity entity) {
    if (entity.getId() == null) {
      entity.setId(UUID.randomUUID());
    }
    jdbcTemplate().update(
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
    List<UserEntity> result = jdbcTemplate().query(
        "SELECT BIN_TO_UUID(id, true) AS id, username, firstname, lastname, avatar "
            + "FROM `user` WHERE id = UUID_TO_BIN(?, true)",
        UserEntityRowMapper.instance,
        id.toString()
    );
    return result.stream().findFirst();
  }

  @Override
  public Optional<UserEntity> findByUsername(String username) {
    List<UserEntity> result = jdbcTemplate().query(
        "SELECT BIN_TO_UUID(id, true) AS id, username, firstname, lastname, avatar "
            + "FROM `user` WHERE username = ?",
        UserEntityRowMapper.instance,
        username
    );
    return result.stream().findFirst();
  }

  @Override
  public List<UserEntity> findAll() {
    return jdbcTemplate().query(
        "SELECT BIN_TO_UUID(id, true) AS id, username, firstname, lastname, avatar "
            + "FROM `user`",
        UserEntityRowMapper.instance
    );
  }

  @Override
  public UserEntity update(UserEntity entity) {
    jdbcTemplate().update(
        "UPDATE `user` SET username = ?, firstname = ?, lastname = ?, avatar = ? "
            + "WHERE id = UUID_TO_BIN(?, true)",
        entity.getUsername(),
        entity.getFirstname(),
        entity.getLastname(),
        entity.getAvatar() != null ? entity.getAvatar().getBytes() : null,
        entity.getId().toString()
    );
    return entity;
  }

  @Override
  public void delete(UserEntity entity) {
    jdbcTemplate().update(
        "DELETE FROM `user` WHERE id = UUID_TO_BIN(?, true)",
        entity.getId().toString()
    );
  }
}
