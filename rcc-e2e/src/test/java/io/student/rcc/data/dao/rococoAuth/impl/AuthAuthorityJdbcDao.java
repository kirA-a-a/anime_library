package io.student.rcc.data.dao.rococoAuth.impl;

import io.student.rcc.data.dao.rococoAuth.AuthAuthorityDao;
import io.student.rcc.data.dao.rococoAuth.entity.AuthAuthorityEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class AuthAuthorityJdbcDao implements AuthAuthorityDao {

  private final Connection connection;

  public AuthAuthorityJdbcDao(Connection connection) {
    this.connection = connection;
  }

  @Override
  public AuthAuthorityEntity create(AuthAuthorityEntity entity) {
    try (PreparedStatement ps = connection.prepareStatement(
        "INSERT INTO `authority`(id, user_id, authority) "
            + "VALUES (UUID_TO_BIN(?, true), UUID_TO_BIN(?, true), ?)"
    )) {
      if (entity.getId() == null) {
        entity.setId(UUID.randomUUID());
      }
      ps.setString(1, entity.getId().toString());
      ps.setString(2, entity.getUserId().toString());
      ps.setString(3, entity.getAuthority());

      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return entity;
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
