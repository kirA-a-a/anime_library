package io.student.rcc.data.rococoApi.repository.iml;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoApi.entity.PaintingEntity;
import io.student.rcc.data.rococoApi.mapper.PaintingWithArtistExtractor;
import io.student.rcc.data.rococoApi.repository.PaintingRepository;
import io.student.rcc.data.tpl.DataSources;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;

public class PaintingRepositorySpringJdbc implements PaintingRepository {

  private static final Config CFG = Config.getInstance();

  private static final String PAINTING_WITH_ARTIST_SELECT =
      "SELECT BIN_TO_UUID(p.id, true) AS id, p.title, p.description, "
          + "BIN_TO_UUID(p.artist_id, true) AS artist_id, "
          + "BIN_TO_UUID(p.museum_id, true) AS museum_id, p.content, "
          + "a.name AS artist_name, a.biography AS artist_biography, a.photo AS artist_photo "
          + "FROM `painting` p "
          + "JOIN `artist` a ON p.artist_id = a.id";

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
        PAINTING_WITH_ARTIST_SELECT + " WHERE p.id = UUID_TO_BIN(?, true)",
        PaintingWithArtistExtractor.instance,
        id.toString()
    );
    return result.stream().findFirst();
  }

  @Override
  public List<PaintingEntity> findAll() {
    return jdbcTemplate().query(PAINTING_WITH_ARTIST_SELECT, PaintingWithArtistExtractor.instance);
  }

  @Override
  public Optional<PaintingEntity> findByTitle(String title) {
    List<PaintingEntity> result = jdbcTemplate().query(
        PAINTING_WITH_ARTIST_SELECT + " WHERE p.title = ?",
        PaintingWithArtistExtractor.instance,
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
