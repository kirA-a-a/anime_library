package io.student.rcc.data.rococoAuth.impl;

import static io.student.rcc.data.tpl.Connections.holder;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoAuth.AuthUserDao;
import io.student.rcc.data.rococoAuth.entity.AuthUserEntity;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthUserJdbcDao implements AuthUserDao {

  static final Config CFG = Config.getInstance();
  private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  @Override
  public AuthUserEntity create(AuthUserEntity user) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "INSERT INTO `user` "
            + "(id, username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)"
            + " VALUES (UUID_TO_BIN(?, true), ?, ?, ?, ?, ?, ?)"
    )) {
      if (user.getId() == null) {
        user.setId(UUID.randomUUID());
      }
      ps.setString(1, user.getId().toString());
      ps.setString(2, user.getUsername());
      ps.setString(3, passwordEncoder.encode(user.getPassword()));
      ps.setBoolean(4, user.isEnabled());
      ps.setBoolean(5, user.isAccountNonExpired());
      ps.setBoolean(6, user.isAccountNonLocked());
      ps.setBoolean(7, user.isCredentialsNonExpired());
      ps.executeUpdate();
      return user;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<AuthUserEntity> findById(UUID id) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "SELECT BIN_TO_UUID(id, true) as id, username, password, enabled, "
            + "account_non_expired, account_non_locked, credentials_non_expired "
            + "FROM `user` WHERE id = UUID_TO_BIN(?, true)"
    )) {
      ps.setString(1, id.toString());

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapAuthUser(rs));
        }
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<AuthUserEntity> findAll() {

    List<AuthUserEntity> userEntities = new ArrayList<>();

    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "SELECT BIN_TO_UUID(id, true) AS id, username, password, enabled, "
            + "account_non_expired, account_non_locked, credentials_non_expired "
            + "FROM `user`"
    )) {

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          userEntities.add(mapAuthUser(rs));
        }
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return userEntities;
  }

  @Override
  public Optional<AuthUserEntity> findByUsername(String username) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "SELECT BIN_TO_UUID(id, true) as id, username, password, enabled, "
            + "account_non_expired, account_non_locked, credentials_non_expired "
            + "FROM `user` WHERE username = ?"
    )) {
      ps.setString(1, username);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapAuthUser(rs));
        }
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public AuthUserEntity update(AuthUserEntity user) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "UPDATE `user` SET username = ?, password = ?, enabled = ?, "
            + "account_non_expired = ?, account_non_locked = ?, credentials_non_expired = ? "
            + "WHERE id = UUID_TO_BIN(?, true)"
    )) {
      ps.setString(1, user.getUsername());
      ps.setString(2, passwordEncoder.encode(user.getPassword()));
      ps.setBoolean(3, user.isEnabled());
      ps.setBoolean(4, user.isAccountNonExpired());
      ps.setBoolean(5, user.isAccountNonLocked());
      ps.setBoolean(6, user.isCredentialsNonExpired());
      ps.setString(7, user.getId().toString());
      ps.executeUpdate();
      return user;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(AuthUserEntity user) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "DELETE FROM `user` WHERE id = UUID_TO_BIN(?, true)"
    )) {
      ps.setString(1, user.getId().toString());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private static AuthUserEntity mapAuthUser(ResultSet rs) throws SQLException {
    AuthUserEntity entity = new AuthUserEntity();
    entity.setId(UUID.fromString(rs.getString("id")));
    entity.setUsername(rs.getString("username"));
    entity.setPassword(rs.getString("password"));
    entity.setEnabled(rs.getBoolean("enabled"));
    entity.setAccountNonExpired(rs.getBoolean("account_non_expired"));
    entity.setAccountNonLocked(rs.getBoolean("account_non_locked"));
    entity.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
    return entity;
  }
}
