package io.student.rcc.data.rococoApi.impl;

import io.student.rcc.data.rococoApi.ArtistDao;
import io.student.rcc.data.rococoApi.entity.ArtistEntity;
import io.student.rcc.data.rococoApi.mapper.ArtistEntityRowMapper;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class ArtistDaoSpringJdbc implements ArtistDao {

  private final DataSource dataSource;

  public ArtistDaoSpringJdbc(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public ArtistEntity create(ArtistEntity entity) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(
        con -> {
          PreparedStatement ps = con.prepareStatement(
              "INSERT INTO `artist`(name, biography, photo) VALUES (?, ?, ?)",
              Statement.RETURN_GENERATED_KEYS
          );
          ps.setString(1, entity.getName());
          ps.setString(2, entity.getBiography());
          ps.setBytes(3, entity.getPhoto());
          return ps;
        }, keyHolder
    );

    final UUID generatedKey = (UUID) keyHolder.getKeys().get("id");
    entity.setId(generatedKey);
    return entity;
  }

  @Override
  public Optional<ArtistEntity> findById(UUID id) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            "SELECT BIN_TO_UUID(id, true) as id, name, biography, photo "
                + "FROM `artist` WHERE id = UUID_TO_BIN(?, true)",
            ArtistEntityRowMapper.instance,
            id.toString()
        )
    );
  }

  @Override
  public List<ArtistEntity> findAll() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate.query(
        "SELECT BIN_TO_UUID(id, true) as id, name, biography, photo FROM `artist`",
        ArtistEntityRowMapper.instance
    );
  }

  @Override
  public Optional<ArtistEntity> findByName(String name) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            "SELECT BIN_TO_UUID(id, true) as id, name, biography, photo "
                + "FROM `artist` WHERE name = ?",
            ArtistEntityRowMapper.instance,
            name
        )
    );
  }

  @Override
  public ArtistEntity update(ArtistEntity entity) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.update(
        "UPDATE `artist` SET name = ?, biography = ?, photo = ? WHERE id = UUID_TO_BIN(?, true)",
        ps -> {
          ps.setString(1, entity.getName());
          ps.setString(2, entity.getBiography());
          ps.setBytes(3, entity.getPhoto());
          ps.setString(4, entity.getId().toString());
        }
    );
    return entity;
  }

  @Override
  public void delete(ArtistEntity entity) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.update(
        "DELETE FROM `artist` WHERE id = UUID_TO_BIN(?, true)",
        entity.getId().toString()
    );
  }
}
