package io.student.rcc.data.rococoApi;

import io.student.rcc.data.rococoApi.entity.ArtistEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class ArtistJdbcDao implements ArtistDao {

  private final Connection connection;

  public ArtistJdbcDao(Connection connection) {
    this.connection = connection;
  }

  @Override
  public ArtistEntity create(ArtistEntity entity) {
    try (PreparedStatement ps = connection.prepareStatement(
        "INSERT INTO `artist`(id, name, biography, photo) "
            + "VALUES (UUID_TO_BIN(?, true), ?, ?, ?)"
    )) {
      if (entity.getId() == null) {
        entity.setId(UUID.randomUUID());
      }
      ps.setString(1, entity.getId().toString());
      ps.setString(2, entity.getName());
      ps.setString(3, entity.getBiography());
      ps.setBytes(4, entity.getPhoto() != null ? entity.getPhoto().getBytes() : null);

      ps.executeUpdate();
      return entity;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<ArtistEntity> findById(UUID id) {
    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT BIN_TO_UUID(id, true) as id, name, biography, photo "
            + "FROM `artist` WHERE id = UUID_TO_BIN(?, true)"
    )) {
      ps.setString(1, id.toString());

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapArtist(rs));
        }
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<ArtistEntity> findByName(String name) {
    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT BIN_TO_UUID(id, true) as id, name, biography, photo "
            + "FROM `artist` WHERE name = ?"
    )) {
      ps.setString(1, name);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapArtist(rs));
        }
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public ArtistEntity update(ArtistEntity entity) {
    try (PreparedStatement ps = connection.prepareStatement(
        "UPDATE `artist` SET name = ?, biography = ?, photo = ? "
            + "WHERE id = UUID_TO_BIN(?, true)"
    )) {
      ps.setString(1, entity.getName());
      ps.setString(2, entity.getBiography());
      ps.setBytes(3, entity.getPhoto() != null ? entity.getPhoto().getBytes() : null);
      ps.setString(4, entity.getId().toString());

      ps.executeUpdate();
      return entity;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(ArtistEntity entity) {
    try (PreparedStatement ps = connection.prepareStatement(
        "DELETE FROM `artist` WHERE id = UUID_TO_BIN(?, true)"
    )) {
      ps.setString(1, entity.getId().toString());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private static ArtistEntity mapArtist(ResultSet rs) throws SQLException {
    ArtistEntity entity = new ArtistEntity();
    entity.setId(UUID.fromString(rs.getString("id")));
    entity.setName(rs.getString("name"));
    entity.setBiography(rs.getString("biography"));
    byte[] photo = rs.getBytes("photo");
    entity.setPhoto(photo != null ? new String(photo) : null);
    return entity;
  }
}
