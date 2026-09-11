package io.student.rcc.data.rococoApi.impl;

import io.student.rcc.data.rococoApi.PaintingDao;
import io.student.rcc.data.rococoApi.entity.PaintingEntity;
import io.student.rcc.data.rococoApi.mapper.PaintingEntityRowMapper;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class PaintingSpringJdbcDao implements PaintingDao {

  private final DataSource dataSource;

  public PaintingSpringJdbcDao(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public PaintingEntity create(PaintingEntity entity) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(
        con -> {
          PreparedStatement ps = con.prepareStatement(
              "INSERT INTO `painting`(title, description, artist_id, museum_id, content) "
                  + "VALUES (?, ?, UUID_TO_BIN(?, true), UUID_TO_BIN(?, true), ?)",
              Statement.RETURN_GENERATED_KEYS
          );
          ps.setString(1, entity.getTitle());
          ps.setString(2, entity.getDescription());
          ps.setString(3, entity.getArtist().getId().toString());
          ps.setString(4, entity.getMuseum() != null ? entity.getMuseum().getId().toString() : null);
          ps.setBytes(5, entity.getContent());
          return ps;
        }, keyHolder
    );

    final UUID generatedKey = (UUID) keyHolder.getKeys().get("id");
    entity.setId(generatedKey);
    return entity;
  }

  @Override
  public Optional<PaintingEntity> findById(UUID id) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            "SELECT BIN_TO_UUID(id, true) as id, title, description, "
                + "BIN_TO_UUID(artist_id, true) as artist_id, "
                + "BIN_TO_UUID(museum_id, true) as museum_id, content "
                + "FROM `painting` WHERE id = UUID_TO_BIN(?, true)",
            PaintingEntityRowMapper.instance,
            id.toString()
        )
    );
  }

  @Override
  public List<PaintingEntity> findAll() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return jdbcTemplate.query(
        "SELECT BIN_TO_UUID(id, true) as id, title, description, "
            + "BIN_TO_UUID(artist_id, true) as artist_id, "
            + "BIN_TO_UUID(museum_id, true) as museum_id, content "
            + "FROM `painting`",
        PaintingEntityRowMapper.instance
    );
  }

  @Override
  public Optional<PaintingEntity> findByTitle(String title) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    return Optional.ofNullable(
        jdbcTemplate.queryForObject(
            "SELECT BIN_TO_UUID(id, true) as id, title, description, "
                + "BIN_TO_UUID(artist_id, true) as artist_id, "
                + "BIN_TO_UUID(museum_id, true) as museum_id, content "
                + "FROM `painting` WHERE title = ?",
            PaintingEntityRowMapper.instance,
            title
        )
    );
  }

  @Override
  public PaintingEntity update(PaintingEntity entity) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.update(
        "UPDATE `painting` SET title = ?, description = ?, "
            + "artist_id = UUID_TO_BIN(?, true), museum_id = UUID_TO_BIN(?, true), content = ? "
            + "WHERE id = UUID_TO_BIN(?, true)",
        ps -> {
          ps.setString(1, entity.getTitle());
          ps.setString(2, entity.getDescription());
          ps.setString(3, entity.getArtist().getId().toString());
          ps.setString(4, entity.getMuseum() != null ? entity.getMuseum().getId().toString() : null);
          ps.setBytes(5, entity.getContent());
          ps.setString(6, entity.getId().toString());
        }
    );
    return entity;
  }

  @Override
  public void delete(PaintingEntity entity) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.update(
        "DELETE FROM `painting` WHERE id = UUID_TO_BIN(?, true)",
        entity.getId().toString()
    );
  }
}
