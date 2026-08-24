package io.student.rcc.data.rococoApi;

import io.student.rcc.data.rococoApi.entity.PaintingEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class PaintingJdbcDao implements PaintingDao {

  private final Connection connection;

  public PaintingJdbcDao(Connection connection) {
    this.connection = connection;
  }

  @Override
  public PaintingEntity create(PaintingEntity entity) {
    try (PreparedStatement ps = connection.prepareStatement(
        "INSERT INTO `painting`(id, title, description, artist_id, museum_id, content) "
            + "VALUES (UUID_TO_BIN(?, true), ?, ?, UUID_TO_BIN(?, true), UUID_TO_BIN(?, true), ?)"
    )) {
      if (entity.getId() == null) {
        entity.setId(UUID.randomUUID());
      }
      ps.setString(1, entity.getId().toString());
      ps.setString(2, entity.getTitle());
      ps.setString(3, entity.getDescription());
      ps.setString(4, entity.getArtistId().toString());
      ps.setString(5, entity.getMuseumId() != null ? entity.getMuseumId().toString() : null);
      ps.setBytes(6, entity.getContent() != null ? entity.getContent().getBytes() : null);

      ps.executeUpdate();
      return entity;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<PaintingEntity> findById(UUID id) {
    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT BIN_TO_UUID(id, true) as id, title, description, "
            + "BIN_TO_UUID(artist_id, true) as artist_id, "
            + "BIN_TO_UUID(museum_id, true) as museum_id, content "
            + "FROM `painting` WHERE id = UUID_TO_BIN(?, true)"
    )) {
      ps.setString(1, id.toString());

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapPainting(rs));
        }
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<PaintingEntity> findByTitle(String title) {
    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT BIN_TO_UUID(id, true) as id, title, description, "
            + "BIN_TO_UUID(artist_id, true) as artist_id, "
            + "BIN_TO_UUID(museum_id, true) as museum_id, content "
            + "FROM `painting` WHERE title = ?"
    )) {
      ps.setString(1, title);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapPainting(rs));
        }
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public PaintingEntity update(PaintingEntity entity) {
    try (PreparedStatement ps = connection.prepareStatement(
        "UPDATE `painting` SET title = ?, description = ?, "
            + "artist_id = UUID_TO_BIN(?, true), museum_id = UUID_TO_BIN(?, true), content = ? "
            + "WHERE id = UUID_TO_BIN(?, true)"
    )) {
      ps.setString(1, entity.getTitle());
      ps.setString(2, entity.getDescription());
      ps.setString(3, entity.getArtistId().toString());
      ps.setString(4, entity.getMuseumId() != null ? entity.getMuseumId().toString() : null);
      ps.setBytes(5, entity.getContent() != null ? entity.getContent().getBytes() : null);
      ps.setString(6, entity.getId().toString());

      ps.executeUpdate();
      return entity;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(PaintingEntity entity) {
    try (PreparedStatement ps = connection.prepareStatement(
        "DELETE FROM `painting` WHERE id = UUID_TO_BIN(?, true)"
    )) {
      ps.setString(1, entity.getId().toString());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private static PaintingEntity mapPainting(ResultSet rs) throws SQLException {
    PaintingEntity entity = new PaintingEntity();
    entity.setId(UUID.fromString(rs.getString("id")));
    entity.setTitle(rs.getString("title"));
    entity.setDescription(rs.getString("description"));
    entity.setArtistId(UUID.fromString(rs.getString("artist_id")));
    String museumId = rs.getString("museum_id");
    entity.setMuseumId(museumId != null ? UUID.fromString(museumId) : null);
    byte[] content = rs.getBytes("content");
    entity.setContent(content != null ? new String(content) : null);
    return entity;
  }
}
