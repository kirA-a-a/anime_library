package io.student.rcc.data.rococoApi.impl;

import io.student.rcc.data.rococoApi.CountryDao;
import io.student.rcc.data.rococoApi.entity.CountryEntity;
import io.student.rcc.data.rococoApi.mapper.CountryEntityRowMapper;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class CountryDaoSpringJdbc implements CountryDao {

  private final DataSource dataSource;

  public CountryDaoSpringJdbc(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public CountryEntity create(CountryEntity entity) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(
        con -> {
          PreparedStatement ps = con.prepareStatement(
              "INSERT INTO `country`(name) VALUES (?)",
              Statement.RETURN_GENERATED_KEYS
          );
          ps.setString(1, entity.getName());
          return ps;
        }, keyHolder
    );

    final UUID generatedKey = (UUID) keyHolder.getKeys().get("id");
    entity.setId(generatedKey);
    return entity;
  }

  @Override
  public Optional<CountryEntity> findById(UUID id) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            "SELECT BIN_TO_UUID(id, true) as id, name FROM `country` WHERE id = UUID_TO_BIN(?, true)",
            CountryEntityRowMapper.instance,
            id.toString()
        )
    );
  }

  @Override
  public List<CountryEntity> findAll() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate.query(
        "SELECT BIN_TO_UUID(id, true) as id, name FROM `country`",
        CountryEntityRowMapper.instance
    );
  }

  @Override
  public Optional<CountryEntity> findByName(String name) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            "SELECT BIN_TO_UUID(id, true) as id, name FROM `country` WHERE name = ?",
            CountryEntityRowMapper.instance,
            name
        )
    );
  }

  @Override
  public CountryEntity update(CountryEntity entity) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.update(
        "UPDATE `country` SET name = ? WHERE id = UUID_TO_BIN(?, true)",
        entity.getName(),
        entity.getId().toString()
    );
    return entity;
  }

  @Override
  public void delete(CountryEntity entity) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.update(
        "DELETE FROM `country` WHERE id = UUID_TO_BIN(?, true)",
        entity.getId().toString()
    );
  }
}
