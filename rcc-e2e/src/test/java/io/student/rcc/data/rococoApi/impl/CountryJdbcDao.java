package io.student.rcc.data.rococoApi.impl;

import io.student.rcc.data.rococoApi.CountryDao;
import io.student.rcc.data.rococoApi.entity.CountryEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CountryJdbcDao implements CountryDao {

  private final Connection connection;

  public CountryJdbcDao(Connection connection) {
    this.connection = connection;
  }

  @Override
  public CountryEntity create(CountryEntity entity) {
    try (PreparedStatement ps = connection.prepareStatement(
        "INSERT INTO `country`(id, name) VALUES (UUID_TO_BIN(?, true), ?)"
    )) {
      if (entity.getId() == null) {
        entity.setId(UUID.randomUUID());
      }
      ps.setString(1, entity.getId().toString());
      ps.setString(2, entity.getName());

      ps.executeUpdate();
      return entity;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<CountryEntity> findById(UUID id) {
    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT BIN_TO_UUID(id, true) as id, name FROM `country` WHERE id = UUID_TO_BIN(?, true)"
    )) {
      ps.setString(1, id.toString());

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapCountry(rs));
        }
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<CountryEntity> findAll() {
    List<CountryEntity> countries = new ArrayList<>();

    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT BIN_TO_UUID(id, true) as id, name FROM `country`"
    )) {
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          countries.add(mapCountry(rs));
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return countries;
  }

  @Override
  public Optional<CountryEntity> findByName(String name) {
    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT BIN_TO_UUID(id, true) as id, name FROM `country` WHERE name = ?"
    )) {
      ps.setString(1, name);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapCountry(rs));
        }
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public CountryEntity update(CountryEntity entity) {
    try (PreparedStatement ps = connection.prepareStatement(
        "UPDATE `country` SET name = ? WHERE id = UUID_TO_BIN(?, true)"
    )) {
      ps.setString(1, entity.getName());
      ps.setString(2, entity.getId().toString());

      ps.executeUpdate();
      return entity;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(CountryEntity entity) {
    try (PreparedStatement ps = connection.prepareStatement(
        "DELETE FROM `country` WHERE id = UUID_TO_BIN(?, true)"
    )) {
      ps.setString(1, entity.getId().toString());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private static CountryEntity mapCountry(ResultSet rs) throws SQLException {
    CountryEntity entity = new CountryEntity();
    entity.setId(UUID.fromString(rs.getString("id")));
    entity.setName(rs.getString("name"));
    return entity;
  }
}
