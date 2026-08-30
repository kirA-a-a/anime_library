package io.student.rcc.data.dao.rococoAuth.impl;

import io.student.rcc.data.dao.rococoAuth.AuthAuthorityDao;
import io.student.rcc.data.dao.rococoAuth.entity.AuthAuthorityEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class AuthAuthorityJdbcDao implements AuthAuthorityDao {

  private final Connection connection;

  public AuthAuthorityJdbcDao(Connection connection) {
    this.connection = connection;
  }

  @Override
  public void create(AuthAuthorityEntity... entities) {
    try (PreparedStatement ps = connection.prepareStatement(
        "INSERT INTO `authority`(id, user_id, authority) "
            + "VALUES (UUID_TO_BIN(?, true), UUID_TO_BIN(?, true), ?)"
    )) {
      for (AuthAuthorityEntity entity : entities) {
        if (entity.getId() == null) {
          entity.setId(UUID.randomUUID());
        }
        ps.setString(1, entity.getId().toString());
        ps.setString(2, entity.getUserId().toString());
        ps.setString(3, entity.getAuthority().dbValue());
        ps.addBatch();
      }
      ps.executeBatch();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<AuthAuthorityEntity> findAll() {

    List<AuthAuthorityEntity> authorities = new ArrayList<>();

    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT BIN_TO_UUID(id, true) AS id, "
            + "BIN_TO_UUID(user_id, true) AS user_id, authority "
            + "FROM `authority`"
    )) {

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          AuthAuthorityEntity entity = new AuthAuthorityEntity();

          entity.setId(UUID.fromString(rs.getString("id")));
          entity.setUserId(UUID.fromString(rs.getString("user_id")));
          entity.setAuthority(
              AuthAuthorityEntity.Authority.valueOf(
                  rs.getString("authority").toUpperCase()
              )
          );

          authorities.add(entity);
        }
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return authorities;
  }

  @Override
  public void deleteByUserId(UUID userId) {
    try (PreparedStatement ps = connection.prepareStatement(
        "DELETE FROM `authority` WHERE user_id = UUID_TO_BIN(?, true)"
    )) {
      ps.setString(1, userId.toString());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
