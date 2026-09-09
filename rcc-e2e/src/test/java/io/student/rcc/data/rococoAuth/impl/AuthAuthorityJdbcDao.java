package io.student.rcc.data.rococoAuth.impl;

import static io.student.rcc.data.tpl.Connections.holder;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoAuth.AuthAuthorityDao;
import io.student.rcc.data.rococoAuth.entity.AuthAuthorityEntity;
import io.student.rcc.data.rococoAuth.entity.AuthUserEntity;
import io.student.rcc.data.rococoAuth.entity.Authority;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuthAuthorityJdbcDao implements AuthAuthorityDao {

  static final Config CFG = Config.getInstance();

  @Override
  public void create(AuthAuthorityEntity... entities) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "INSERT INTO `authority`(id, user_id, authority) "
            + "VALUES (UUID_TO_BIN(?, true), UUID_TO_BIN(?, true), ?)"
    )) {
      for (AuthAuthorityEntity entity : entities) {
        if (entity.getId() == null) {
          entity.setId(UUID.randomUUID());
        }
        ps.setString(1, entity.getId().toString());
        ps.setString(2, entity.getUser().getId().toString());
        ps.setString(3, entity.getAuthority().name());
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

    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "SELECT BIN_TO_UUID(id, true) AS id, "
            + "BIN_TO_UUID(user_id, true) AS user_id, authority "
            + "FROM `authority`"
    )) {

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          AuthAuthorityEntity entity = new AuthAuthorityEntity();
          entity.setId(UUID.fromString(rs.getString("id")));

          AuthUserEntity user = new AuthUserEntity();
          user.setId(UUID.fromString(rs.getString("user_id")));
          entity.setUser(user);
          entity.setAuthority(Authority.valueOf(rs.getString("authority")));

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
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "DELETE FROM `authority` WHERE user_id = UUID_TO_BIN(?, true)"
    )) {
      ps.setString(1, userId.toString());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
