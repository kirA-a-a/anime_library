package io.student.rcc.data.dao.rococoAuth.impl;

import io.student.rcc.data.dao.rococoAuth.UserDao;
import io.student.rcc.data.dao.rococoAuth.entity.UserEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class UserJdbcDao implements UserDao {

  private final Connection connection;

  public UserJdbcDao(Connection connection) {
    this.connection = connection;
  }

  @Override
  public UserEntity create(UserEntity entity) {
    try (PreparedStatement ps = connection.prepareStatement(
        "INSERT INTO `user`(id, username, firstname, lastname, avatar) "
            + "VALUES (UUID_TO_BIN(?, true), ?, ?, ?, ?)"
    )) {
      if (entity.getId() == null) {
        entity.setId(UUID.randomUUID());
      }
      ps.setString(1, entity.getId().toString());
      ps.setString(2, entity.getUsername());
      ps.setString(3, entity.getFirstname());
      ps.setString(4, entity.getLastname());
      ps.setBytes(5, entity.getAvatar() != null ? entity.getAvatar().getBytes() : null);

      ps.executeUpdate();
      return entity;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<UserEntity> findById(UUID id) {
    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT BIN_TO_UUID(id, true) as id, username, firstname, lastname, avatar "
            + "FROM `user` WHERE id = UUID_TO_BIN(?, true)"
    )) {
      ps.setString(1, id.toString());

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapUser(rs));
        }
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<UserEntity> findByUsername(String username) {
    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT BIN_TO_UUID(id, true) as id, username, firstname, lastname, avatar "
            + "FROM `user` WHERE username = ?"
    )) {
      ps.setString(1, username);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapUser(rs));
        }
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(UserEntity entity) {
    try (PreparedStatement ps = connection.prepareStatement(
        "DELETE FROM `user` WHERE id = UUID_TO_BIN(?, true)"
    )) {
      ps.setString(1, entity.getId().toString());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private static UserEntity mapUser(ResultSet rs) throws SQLException {
    UserEntity entity = new UserEntity();
    entity.setId(UUID.fromString(rs.getString("id")));
    entity.setUsername(rs.getString("username"));
    entity.setFirstname(rs.getString("firstname"));
    entity.setLastname(rs.getString("lastname"));
    byte[] avatar = rs.getBytes("avatar");
    entity.setAvatar(avatar != null ? new String(avatar) : null);
    return entity;
  }
}
