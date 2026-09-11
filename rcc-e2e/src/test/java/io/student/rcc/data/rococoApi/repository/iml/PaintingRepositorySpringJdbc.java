package io.student.rcc.data.rococoApi.repository.iml;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoApi.entity.PaintingEntity;
import io.student.rcc.data.rococoApi.mapper.PaintingEntityRowMapper;
import io.student.rcc.data.rococoApi.repository.PaintingRepository;
import io.student.rcc.data.tpl.DataSources;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;

public class PaintingRepositorySpringJdbc implements PaintingRepository {

  private static final Config CFG = Config.getInstance();

  private static final String PAINTING_SELECT =
      "SELECT BIN_TO_UUID(id, true) AS id, title, description, "
          + "BIN_TO_UUID(artist_id, true) AS artist_id, "
          + "BIN_TO_UUID(museum_id, true) AS museum_id, content "
          + "FROM `painting`";

  private JdbcTemplate jdbcTemplate() {
    return new JdbcTemplate(DataSources.dataSource(CFG.rccApiJdbcUrl()));
  }

  @Override
  public PaintingEntity create(PaintingEntity entity) {
    if (entity.getId() == null) {
      entity.setId(UUID.randomUUID());
    }
    jdbcTemplate().update(
        "INSERT INTO `painting`(id, title, description, artist_id, museum_id, content) "
            + "VALUES (UUID_TO_BIN(?, true), ?, ?, UUID_TO_BIN(?, true), UUID_TO_BIN(?, true), ?)",
        entity.getId().toString(),
        entity.getTitle(),
        entity.getDescription(),
        entity.getArtist().getId().toString(),
        entity.getMuseum() != null ? entity.getMuseum().getId().toString() : null,
        entity.getContent()
    );
    return entity;
  }

  @Override
  public Optional<PaintingEntity> findById(UUID id) {
    List<PaintingEntity> result = jdbcTemplate().query(
        PAINTING_SELECT + " WHERE id = UUID_TO_BIN(?, true)",
        PaintingEntityRowMapper.instance,
        id.toString()
    );
    return result.stream().findFirst();
  }

  @Override
  public List<PaintingEntity> findAll() {
    return jdbcTemplate().query(PAINTING_SELECT, PaintingEntityRowMapper.instance);
  }

  @Override
  public Optional<PaintingEntity> findByTitle(String title) {
    List<PaintingEntity> result = jdbcTemplate().query(
        PAINTING_SELECT + " WHERE title = ?",
        PaintingEntityRowMapper.instance,
        title
    );
    return result.stream().findFirst();
  }

  @Override
  public PaintingEntity update(PaintingEntity entity) {
    jdbcTemplate().update(
        "UPDATE `painting` SET title = ?, description = ?, "
            + "artist_id = UUID_TO_BIN(?, true), museum_id = UUID_TO_BIN(?, true), content = ? "
            + "WHERE id = UUID_TO_BIN(?, true)",
        entity.getTitle(),
        entity.getDescription(),
        entity.getArtist().getId().toString(),
        entity.getMuseum() != null ? entity.getMuseum().getId().toString() : null,
        entity.getContent(),
        entity.getId().toString()
    );
    return entity;
  }

  @Override
  public void delete(PaintingEntity entity) {
    jdbcTemplate().update(
        "DELETE FROM `painting` WHERE id = UUID_TO_BIN(?, true)",
        entity.getId().toString()
    );
  }
}
