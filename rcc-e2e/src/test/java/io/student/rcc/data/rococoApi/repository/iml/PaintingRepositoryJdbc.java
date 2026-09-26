package io.student.rcc.data.rococoApi.repository.iml;

import static io.student.rcc.data.tpl.Connections.holder;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoApi.entity.ArtistEntity;
import io.student.rcc.data.rococoApi.entity.CountryEntity;
import io.student.rcc.data.rococoApi.entity.MuseumEntity;
import io.student.rcc.data.rococoApi.entity.PaintingEntity;
import io.student.rcc.data.rococoApi.repository.PaintingRepository;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PaintingRepositoryJdbc implements PaintingRepository {

  private static final Config CFG = Config.getInstance();

  private static final String PAINTING_WITH_RELATIONS_SELECT =
      "SELECT BIN_TO_UUID(p.id, true) AS id, p.title, p.description, p.content, "
          + "BIN_TO_UUID(ar.id, true) AS artist_id, ar.name AS artist_name, "
          + "ar.biography AS artist_biography, ar.photo AS artist_photo, "
          + "BIN_TO_UUID(m.id, true) AS museum_id, m.title AS museum_title, "
          + "m.description AS museum_description, m.city AS museum_city, m.photo AS museum_photo, "
          + "BIN_TO_UUID(c.id, true) AS country_id, c.name AS country_name "
          + "FROM `painting` p "
          + "JOIN `artist` ar ON p.artist_id = ar.id "
          + "LEFT JOIN `museum` m ON p.museum_id = m.id "
          + "LEFT JOIN `country` c ON m.country_id = c.id ";

  @Override
  public PaintingEntity create(PaintingEntity entity) {
    try (PreparedStatement ps = holder(CFG.rccApiJdbcUrl()).connection().prepareStatement(
        "INSERT INTO `painting`(id, title, description, artist_id, museum_id, content) "
            + "VALUES (UUID_TO_BIN(?, true), ?, ?, UUID_TO_BIN(?, true), UUID_TO_BIN(?, true), ?)"
    )) {
      if (entity.getId() == null) {
        entity.setId(UUID.randomUUID());
      }
      ps.setString(1, entity.getId().toString());
      ps.setString(2, entity.getTitle());
      ps.setString(3, entity.getDescription());
      ps.setString(4, entity.getArtist().getId().toString());
      ps.setString(5, entity.getMuseum() != null ? entity.getMuseum().getId().toString() : null);
      ps.setBytes(6, entity.getContent());
      ps.executeUpdate();
      return entity;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<PaintingEntity> findById(UUID id) {
    try (PreparedStatement ps = holder(CFG.rccApiJdbcUrl()).connection().prepareStatement(
        PAINTING_WITH_RELATIONS_SELECT + "WHERE p.id = UUID_TO_BIN(?, true)"
    )) {
      ps.setString(1, id.toString());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapPaintingWithRelations(rs));
        }
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<PaintingEntity> findAll() {
    List<PaintingEntity> paintings = new ArrayList<>();
    try (PreparedStatement ps = holder(CFG.rccApiJdbcUrl()).connection().prepareStatement(
        PAINTING_WITH_RELATIONS_SELECT
    )) {
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          paintings.add(mapPaintingWithRelations(rs));
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return paintings;
  }

  @Override
  public Optional<PaintingEntity> findByTitle(String title) {
    try (PreparedStatement ps = holder(CFG.rccApiJdbcUrl()).connection().prepareStatement(
        PAINTING_WITH_RELATIONS_SELECT + "WHERE p.title = ?"
    )) {
      ps.setString(1, title);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapPaintingWithRelations(rs));
        }
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public PaintingEntity update(PaintingEntity entity) {
    try (PreparedStatement ps = holder(CFG.rccApiJdbcUrl()).connection().prepareStatement(
        "UPDATE `painting` SET title = ?, description = ?, "
            + "artist_id = UUID_TO_BIN(?, true), museum_id = UUID_TO_BIN(?, true), content = ? "
            + "WHERE id = UUID_TO_BIN(?, true)"
    )) {
      ps.setString(1, entity.getTitle());
      ps.setString(2, entity.getDescription());
      ps.setString(3, entity.getArtist().getId().toString());
      ps.setString(4, entity.getMuseum() != null ? entity.getMuseum().getId().toString() : null);
      ps.setBytes(5, entity.getContent());
      ps.setString(6, entity.getId().toString());
      ps.executeUpdate();
      return entity;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(PaintingEntity entity) {
    try (PreparedStatement ps = holder(CFG.rccApiJdbcUrl()).connection().prepareStatement(
        "DELETE FROM `painting` WHERE id = UUID_TO_BIN(?, true)"
    )) {
      ps.setString(1, entity.getId().toString());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private static PaintingEntity mapPaintingWithRelations(ResultSet rs) throws SQLException {
    PaintingEntity painting = new PaintingEntity();
    painting.setId(UUID.fromString(rs.getString("id")));
    painting.setTitle(rs.getString("title"));
    painting.setDescription(rs.getString("description"));
    painting.setContent(rs.getBytes("content"));

    ArtistEntity artist = new ArtistEntity();
    artist.setId(UUID.fromString(rs.getString("artist_id")));
    artist.setName(rs.getString("artist_name"));
    artist.setBiography(rs.getString("artist_biography"));
    artist.setPhoto(rs.getBytes("artist_photo"));
    painting.setArtist(artist);

    String museumId = rs.getString("museum_id");
    if (museumId != null) {
      MuseumEntity museum = new MuseumEntity();
      museum.setId(UUID.fromString(museumId));
      museum.setTitle(rs.getString("museum_title"));
      museum.setDescription(rs.getString("museum_description"));
      museum.setCity(rs.getString("museum_city"));
      museum.setPhoto(rs.getBytes("museum_photo"));

      String countryId = rs.getString("country_id");
      if (countryId != null) {
        CountryEntity country = new CountryEntity();
        country.setId(UUID.fromString(countryId));
        country.setName(rs.getString("country_name"));
        museum.setCountry(country);
      }
      painting.setMuseum(museum);
    }

    return painting;
  }
}
