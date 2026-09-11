package io.student.rcc.data.rococoAuth.repository.iml;

import static io.student.rcc.data.tpl.Connections.holder;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoAuth.entity.UserEntity;
import io.student.rcc.data.rococoAuth.repository.UserRepository;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryJdbc implements UserRepository {

  static final Config CFG = Config.getInstance();

  @Override
  public UserEntity create(UserEntity entity) {
    try (PreparedStatement ps = holder(CFG.rccApiJdbcUrl()).connection().prepareStatement(
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
    try (PreparedStatement ps = holder(CFG.rccApiJdbcUrl()).connection().prepareStatement(
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
  public List<UserEntity> findAll() {

    List<UserEntity> users = new ArrayList<>();

    try (PreparedStatement ps = holder(CFG.rccApiJdbcUrl()).connection().prepareStatement(
        "SELECT BIN_TO_UUID(id, true) AS id, username, firstname, lastname, avatar "
            + "FROM `user`"
    )) {
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          users.add(mapUser(rs));
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return users;
  }

  @Override
  public Optional<UserEntity> findByUsername(String username) {
    try (PreparedStatement ps = holder(CFG.rccApiJdbcUrl()).connection().prepareStatement(
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
  public UserEntity update(UserEntity entity) {
    try (PreparedStatement ps = holder(CFG.rccApiJdbcUrl()).connection().prepareStatement(
        "UPDATE `user` SET username = ?, firstname = ?, lastname = ?, avatar = ? "
            + "WHERE id = UUID_TO_BIN(?, true)"
    )) {
      ps.setString(1, entity.getUsername());
      ps.setString(2, entity.getFirstname());
      ps.setString(3, entity.getLastname());
      ps.setBytes(4, entity.getAvatar() != null ? entity.getAvatar().getBytes() : null);
      ps.setString(5, entity.getId().toString());
      ps.executeUpdate();
      return entity;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(UserEntity entity) {
    try (PreparedStatement ps = holder(CFG.rccApiJdbcUrl()).connection().prepareStatement(
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
