package io.student.rcc.data.rococoApi.repository.iml;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoApi.entity.CountryEntity;
import io.student.rcc.data.rococoApi.mapper.CountryEntityRowMapper;
import io.student.rcc.data.rococoApi.repository.CountryRepository;
import io.student.rcc.data.tpl.DataSources;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;

public class CountryRepositorySpringJdbc implements CountryRepository {

  private static final Config CFG = Config.getInstance();

  private JdbcTemplate jdbcTemplate() {
    return new JdbcTemplate(DataSources.dataSource(CFG.rccApiJdbcUrl()));
  }

  @Override
  public CountryEntity create(CountryEntity entity) {
    if (entity.getId() == null) {
      entity.setId(UUID.randomUUID());
    }
    jdbcTemplate().update(
        "INSERT INTO `country`(id, name) VALUES (UUID_TO_BIN(?, true), ?)",
        entity.getId().toString(),
        entity.getName()
    );
    return entity;
  }

  @Override
  public Optional<CountryEntity> findById(UUID id) {
    List<CountryEntity> result = jdbcTemplate().query(
        "SELECT BIN_TO_UUID(id, true) AS id, name FROM `country` WHERE id = UUID_TO_BIN(?, true)",
        CountryEntityRowMapper.instance,
        id.toString()
    );
    return result.stream().findFirst();
  }

  @Override
  public List<CountryEntity> findAll() {
    return jdbcTemplate().query(
        "SELECT BIN_TO_UUID(id, true) AS id, name FROM `country`",
        CountryEntityRowMapper.instance
    );
  }

  @Override
  public Optional<CountryEntity> findByName(String name) {
    List<CountryEntity> result = jdbcTemplate().query(
        "SELECT BIN_TO_UUID(id, true) AS id, name FROM `country` WHERE name = ?",
        CountryEntityRowMapper.instance,
        name
    );
    return result.stream().findFirst();
  }

  @Override
  public CountryEntity update(CountryEntity entity) {
    jdbcTemplate().update(
        "UPDATE `country` SET name = ? WHERE id = UUID_TO_BIN(?, true)",
        entity.getName(),
        entity.getId().toString()
    );
    return entity;
  }

  @Override
  public void delete(CountryEntity entity) {
    jdbcTemplate().update(
        "DELETE FROM `country` WHERE id = UUID_TO_BIN(?, true)",
        entity.getId().toString()
    );
  }
}
