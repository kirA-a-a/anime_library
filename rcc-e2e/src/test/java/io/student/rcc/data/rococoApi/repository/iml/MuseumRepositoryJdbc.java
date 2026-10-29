package io.student.rcc.data.rococoApi.repository.iml;

import static io.student.rcc.data.tpl.Connections.holder;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoApi.entity.CountryEntity;
import io.student.rcc.data.rococoApi.entity.MuseumEntity;
import io.student.rcc.data.rococoApi.repository.MuseumRepository;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MuseumRepositoryJdbc implements MuseumRepository {

  private static final Config CFG = Config.getInstance();

  private static final String MUSEUM_WITH_COUNTRY_SELECT =
      "SELECT BIN_TO_UUID(m.id, true) AS id, m.title, m.description, m.city, m.photo, "
          + "BIN_TO_UUID(c.id, true) AS country_id, c.name AS country_name "
          + "FROM `museum` m "
          + "JOIN `country` c ON m.country_id = c.id ";

  @Override
  public MuseumEntity create(MuseumEntity entity) {
    try (PreparedStatement ps = holder(CFG.rccApiJdbcUrl()).connection().prepareStatement(
        "INSERT INTO `museum`(id, title, description, city, photo, country_id) "
            + "VALUES (UUID_TO_BIN(?, true), ?, ?, ?, ?, UUID_TO_BIN(?, true))"
    )) {
      if (entity.getId() == null) {
        entity.setId(UUID.randomUUID());
      }
      ps.setString(1, entity.getId().toString());
      ps.setString(2, entity.getTitle());
      ps.setString(3, entity.getDescription());
      ps.setString(4, entity.getCity());
      ps.setBytes(5, entity.getPhoto());
      ps.setString(6, entity.getCountry().getId().toString());
      ps.executeUpdate();
      return entity;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<MuseumEntity> findById(UUID id) {
    try (PreparedStatement ps = holder(CFG.rccApiJdbcUrl()).connection().prepareStatement(
        MUSEUM_WITH_COUNTRY_SELECT + "WHERE m.id = UUID_TO_BIN(?, true)"
    )) {
      ps.setString(1, id.toString());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapMuseumWithCountry(rs));
        }
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<MuseumEntity> findAll() {
    List<MuseumEntity> museums = new ArrayList<>();
    try (PreparedStatement ps = holder(CFG.rccApiJdbcUrl()).connection().prepareStatement(
        MUSEUM_WITH_COUNTRY_SELECT
    )) {
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          museums.add(mapMuseumWithCountry(rs));
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return museums;
  }

  @Override
  public Optional<MuseumEntity> findByTitle(String title) {
    try (PreparedStatement ps = holder(CFG.rccApiJdbcUrl()).connection().prepareStatement(
        MUSEUM_WITH_COUNTRY_SELECT + "WHERE m.title = ?"
    )) {
      ps.setString(1, title);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapMuseumWithCountry(rs));
        }
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public MuseumEntity update(MuseumEntity entity) {
    try (PreparedStatement ps = holder(CFG.rccApiJdbcUrl()).connection().prepareStatement(
        "UPDATE `museum` SET title = ?, description = ?, city = ?, photo = ?, "
            + "country_id = UUID_TO_BIN(?, true) WHERE id = UUID_TO_BIN(?, true)"
    )) {
      ps.setString(1, entity.getTitle());
      ps.setString(2, entity.getDescription());
      ps.setString(3, entity.getCity());
      ps.setBytes(4, entity.getPhoto());
      ps.setString(5, entity.getCountry().getId().toString());
      ps.setString(6, entity.getId().toString());
      ps.executeUpdate();
      return entity;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(MuseumEntity entity) {
    try (PreparedStatement ps = holder(CFG.rccApiJdbcUrl()).connection().prepareStatement(
        "DELETE FROM `museum` WHERE id = UUID_TO_BIN(?, true)"
    )) {
      ps.setString(1, entity.getId().toString());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private static MuseumEntity mapMuseumWithCountry(ResultSet rs) throws SQLException {
    MuseumEntity museum = new MuseumEntity();
    museum.setId(UUID.fromString(rs.getString("id")));
    museum.setTitle(rs.getString("title"));
    museum.setDescription(rs.getString("description"));
    museum.setCity(rs.getString("city"));
    museum.setPhoto(rs.getBytes("photo"));

    CountryEntity country = new CountryEntity();
    country.setId(UUID.fromString(rs.getString("country_id")));
    country.setName(rs.getString("country_name"));
    museum.setCountry(country);
    return museum;
  }
}
