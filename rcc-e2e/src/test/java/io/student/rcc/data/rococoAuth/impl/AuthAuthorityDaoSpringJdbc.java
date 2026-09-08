package io.student.rcc.data.rococoAuth.impl;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoAuth.AuthAuthorityDao;
import io.student.rcc.data.rococoAuth.entity.AuthAuthorityEntity;
import io.student.rcc.data.rococoAuth.mapper.AuthAuthorityEntityRowMapper;
import io.student.rcc.data.tpl.DataSources;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

public class AuthAuthorityDaoSpringJdbc implements AuthAuthorityDao {

  private static final Config CFG = Config.getInstance();

  @Override
  public void create(AuthAuthorityEntity... entities) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    jdbcTemplate.batchUpdate(
        "INSERT INTO `authority`(user_id, authority) VALUES (UUID_TO_BIN(?, true), ?)",
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setString(1, entities[i].getUserId().toString());
            ps.setString(2, entities[i].getAuthority().dbValue());
          }

          @Override
          public int getBatchSize() {
            return entities.length;
          }
        }
    );
  }

  @Override
  public List<AuthAuthorityEntity> findAll() {

    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));

    return jdbcTemplate.query(
        "SELECT BIN_TO_UUID(id, true) AS id, "
            + "BIN_TO_UUID(user_id, true) AS user_id, authority "
            + "FROM `authority`",
        AuthAuthorityEntityRowMapper.instance
    );
  }

  @Override
  public void deleteByUserId(UUID userId) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
    jdbcTemplate.update(
        "DELETE FROM `authority` WHERE user_id = UUID_TO_BIN(?, true)",
        userId.toString()
    );
  }
}
