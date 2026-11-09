package io.student.rcc.data.rococoApi.repository.iml;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoApi.entity.MuseumEntity;
import io.student.rcc.data.rococoApi.mapper.MuseumEntityRowMapper;
import io.student.rcc.data.rococoApi.repository.MuseumRepository;
import io.student.rcc.data.tpl.DataSources;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;

public class MuseumRepositorySpringJdbc implements MuseumRepository {

  private static final Config CFG = Config.getInstance();

  private static final String MUSEUM_SELECT =
      "SELECT BIN_TO_UUID(id, true) AS id, title, description, city, photo, "
          + "BIN_TO_UUID(country_id, true) AS country_id FROM `museum`";

  private JdbcTemplate jdbcTemplate() {
    return new JdbcTemplate(DataSources.dataSource(CFG.rccApiJdbcUrl()));
  }

  @Override
  public MuseumEntity create(MuseumEntity entity) {
    if (entity.getId() == null) {
      entity.setId(UUID.randomUUID());
    }
    jdbcTemplate().update(
        "INSERT INTO `museum`(id, title, description, city, photo, country_id) "
            + "VALUES (UUID_TO_BIN(?, true), ?, ?, ?, ?, UUID_TO_BIN(?, true))",
        entity.getId().toString(),
        entity.getTitle(),
        entity.getDescription(),
        entity.getCity(),
        entity.getPhoto(),
        entity.getCountry().getId().toString()
    );
    return entity;
  }

  @Override
  public Optional<MuseumEntity> findById(UUID id) {
    List<MuseumEntity> result = jdbcTemplate().query(
        MUSEUM_SELECT + " WHERE id = UUID_TO_BIN(?, true)",
        MuseumEntityRowMapper.instance,
        id.toString()
    );
    return result.stream().findFirst();
  }

  @Override
  public List<MuseumEntity> findAll() {
    return jdbcTemplate().query(MUSEUM_SELECT, MuseumEntityRowMapper.instance);
  }

  @Override
  public Optional<MuseumEntity> findByTitle(String title) {
    List<MuseumEntity> result = jdbcTemplate().query(
        MUSEUM_SELECT + " WHERE title = ?",
        MuseumEntityRowMapper.instance,
        title
    );
    return result.stream().findFirst();
  }

  @Override
  public MuseumEntity update(MuseumEntity entity) {
    jdbcTemplate().update(
        "UPDATE `museum` SET title = ?, description = ?, city = ?, photo = ?, "
            + "country_id = UUID_TO_BIN(?, true) WHERE id = UUID_TO_BIN(?, true)",
        entity.getTitle(),
        entity.getDescription(),
        entity.getCity(),
        entity.getPhoto(),
        entity.getCountry().getId().toString(),
        entity.getId().toString()
    );
    return entity;
  }

  @Override
  public void delete(MuseumEntity entity) {
    jdbcTemplate().update(
        "DELETE FROM `museum` WHERE id = UUID_TO_BIN(?, true)",
        entity.getId().toString()
    );
  }
}
