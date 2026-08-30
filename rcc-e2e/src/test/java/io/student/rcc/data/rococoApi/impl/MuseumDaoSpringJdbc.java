package io.student.rcc.data.rococoApi.impl;

import io.student.rcc.data.rococoApi.MuseumDao;
import io.student.rcc.data.rococoApi.entity.MuseumEntity;
import io.student.rcc.data.rococoApi.mapper.MuseumEntityRowMapper;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class MuseumDaoSpringJdbc implements MuseumDao {

  private final DataSource dataSource;

  public MuseumDaoSpringJdbc(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public MuseumEntity create(MuseumEntity entity) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(
        con -> {
          PreparedStatement ps = con.prepareStatement(
              "INSERT INTO `museum`(title, description, city, photo, country_id) "
                  + "VALUES (?, ?, ?, ?, UUID_TO_BIN(?, true))",
              Statement.RETURN_GENERATED_KEYS
          );
          ps.setString(1, entity.getTitle());
          ps.setString(2, entity.getDescription());
          ps.setString(3, entity.getCity());
          ps.setBytes(4, entity.getPhoto() != null ? entity.getPhoto().getBytes() : null);
          ps.setString(5, entity.getCountryId().toString());
          return ps;
        }, keyHolder
    );

    final UUID generatedKey = (UUID) keyHolder.getKeys().get("id");
    entity.setId(generatedKey);
    return entity;
  }

  @Override
  public Optional<MuseumEntity> findById(UUID id) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            "SELECT BIN_TO_UUID(id, true) as id, title, description, city, photo, "
                + "BIN_TO_UUID(country_id, true) as country_id "
                + "FROM `museum` WHERE id = UUID_TO_BIN(?, true)",
            MuseumEntityRowMapper.instance,
            id.toString()
        )
    );
  }

  @Override
  public List<MuseumEntity> findAll() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate.query(
        "SELECT BIN_TO_UUID(id, true) as id, title, description, city, photo, "
            + "BIN_TO_UUID(country_id, true) as country_id FROM `museum`",
        MuseumEntityRowMapper.instance
    );
  }

  @Override
  public Optional<MuseumEntity> findByTitle(String title) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            "SELECT BIN_TO_UUID(id, true) as id, title, description, city, photo, "
                + "BIN_TO_UUID(country_id, true) as country_id "
                + "FROM `museum` WHERE title = ?",
            MuseumEntityRowMapper.instance,
            title
        )
    );
  }

  @Override
  public MuseumEntity update(MuseumEntity entity) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.update(
        "UPDATE `museum` SET title = ?, description = ?, city = ?, photo = ?, "
            + "country_id = UUID_TO_BIN(?, true) WHERE id = UUID_TO_BIN(?, true)",
        ps -> {
          ps.setString(1, entity.getTitle());
          ps.setString(2, entity.getDescription());
          ps.setString(3, entity.getCity());
          ps.setBytes(4, entity.getPhoto() != null ? entity.getPhoto().getBytes() : null);
          ps.setString(5, entity.getCountryId().toString());
          ps.setString(6, entity.getId().toString());
        }
    );
    return entity;
  }

  @Override
  public void delete(MuseumEntity entity) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.update(
        "DELETE FROM `museum` WHERE id = UUID_TO_BIN(?, true)",
        entity.getId().toString()
    );
  }
}
