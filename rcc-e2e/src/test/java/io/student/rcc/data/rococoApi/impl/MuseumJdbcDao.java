package io.student.rcc.data.rococoApi.impl;

import io.student.rcc.data.rococoApi.MuseumDao;
import io.student.rcc.data.rococoApi.entity.CountryEntity;
import io.student.rcc.data.rococoApi.entity.MuseumEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MuseumJdbcDao implements MuseumDao {

  private final Connection connection;

  public MuseumJdbcDao(Connection connection) {
    this.connection = connection;
  }

  @Override
  public MuseumEntity create(MuseumEntity entity) {
    try (PreparedStatement ps = connection.prepareStatement(
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
    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT BIN_TO_UUID(id, true) as id, title, description, city, photo, "
            + "BIN_TO_UUID(country_id, true) as country_id "
            + "FROM `museum` WHERE id = UUID_TO_BIN(?, true)"
    )) {
      ps.setString(1, id.toString());

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapMuseum(rs));
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

    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT BIN_TO_UUID(id, true) as id, title, description, city, photo, "
            + "BIN_TO_UUID(country_id, true) as country_id FROM `museum`"
    )) {
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          museums.add(mapMuseum(rs));
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return museums;
  }

  @Override
  public Optional<MuseumEntity> findByTitle(String title) {
    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT BIN_TO_UUID(id, true) as id, title, description, city, photo, "
            + "BIN_TO_UUID(country_id, true) as country_id "
            + "FROM `museum` WHERE title = ?"
    )) {
      ps.setString(1, title);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapMuseum(rs));
        }
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public MuseumEntity update(MuseumEntity entity) {
    try (PreparedStatement ps = connection.prepareStatement(
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
    try (PreparedStatement ps = connection.prepareStatement(
        "DELETE FROM `museum` WHERE id = UUID_TO_BIN(?, true)"
    )) {
      ps.setString(1, entity.getId().toString());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private static MuseumEntity mapMuseum(ResultSet rs) throws SQLException {
    MuseumEntity entity = new MuseumEntity();
    entity.setId(UUID.fromString(rs.getString("id")));
    entity.setTitle(rs.getString("title"));
    entity.setDescription(rs.getString("description"));
    entity.setCity(rs.getString("city"));
    entity.setPhoto(rs.getBytes("photo"));
    CountryEntity country = new CountryEntity();
    country.setId(UUID.fromString(rs.getString("country_id")));
    entity.setCountry(country);
    return entity;
  }
}
