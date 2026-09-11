package io.student.rcc.data.rococoApi.repository.iml;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoApi.entity.ArtistEntity;
import io.student.rcc.data.rococoApi.mapper.ArtistEntityRowMapper;
import io.student.rcc.data.rococoApi.repository.ArtistRepository;
import io.student.rcc.data.tpl.DataSources;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;

public class ArtistRepositorySpringJdbc implements ArtistRepository {

  private static final Config CFG = Config.getInstance();

  private JdbcTemplate jdbcTemplate() {
    return new JdbcTemplate(DataSources.dataSource(CFG.rccApiJdbcUrl()));
  }

  @Override
  public ArtistEntity create(ArtistEntity entity) {
    if (entity.getId() == null) {
      entity.setId(UUID.randomUUID());
    }
    jdbcTemplate().update(
        "INSERT INTO `artist`(id, name, biography, photo) "
            + "VALUES (UUID_TO_BIN(?, true), ?, ?, ?)",
        entity.getId().toString(),
        entity.getName(),
        entity.getBiography(),
        entity.getPhoto()
    );
    return entity;
  }

  @Override
  public Optional<ArtistEntity> findById(UUID id) {
    List<ArtistEntity> result = jdbcTemplate().query(
        "SELECT BIN_TO_UUID(id, true) AS id, name, biography, photo "
            + "FROM `artist` WHERE id = UUID_TO_BIN(?, true)",
        ArtistEntityRowMapper.instance,
        id.toString()
    );
    return result.stream().findFirst();
  }

  @Override
  public List<ArtistEntity> findAll() {
    return jdbcTemplate().query(
        "SELECT BIN_TO_UUID(id, true) AS id, name, biography, photo FROM `artist`",
        ArtistEntityRowMapper.instance
    );
  }

  @Override
  public Optional<ArtistEntity> findByName(String name) {
    List<ArtistEntity> result = jdbcTemplate().query(
        "SELECT BIN_TO_UUID(id, true) AS id, name, biography, photo "
            + "FROM `artist` WHERE name = ?",
        ArtistEntityRowMapper.instance,
        name
    );
    return result.stream().findFirst();
  }

  @Override
  public ArtistEntity update(ArtistEntity entity) {
    jdbcTemplate().update(
        "UPDATE `artist` SET name = ?, biography = ?, photo = ? "
            + "WHERE id = UUID_TO_BIN(?, true)",
        entity.getName(),
        entity.getBiography(),
        entity.getPhoto(),
        entity.getId().toString()
    );
    return entity;
  }

  @Override
  public void delete(ArtistEntity entity) {
    jdbcTemplate().update(
        "DELETE FROM `artist` WHERE id = UUID_TO_BIN(?, true)",
        entity.getId().toString()
    );
  }
}
