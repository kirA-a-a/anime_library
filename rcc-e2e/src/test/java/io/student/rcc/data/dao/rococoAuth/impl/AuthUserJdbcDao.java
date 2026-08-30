package io.student.rcc.data.dao.rococoAuth.impl;

import io.student.rcc.data.dao.rococoAuth.AuthUserDao;
import io.student.rcc.data.dao.rococoAuth.entity.AuthAuthorityEntity;
import io.student.rcc.data.dao.rococoAuth.entity.AuthUserEntity;
import java.sql.Connection;
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

  private final Connection connection;
  private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  public AuthUserJdbcDao(Connection connection) {
    this.connection = connection;
  }

  @Override
  public AuthUserEntity create(AuthUserEntity user) {
    try (PreparedStatement ps = connection.prepareStatement("INSERT INTO `user` "
        + "(id, username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)"
        + " VALUES (UUID_TO_BIN(?, true), ?, ?, ?, ?, ?, ?)")) {
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
    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT BIN_TO_UUID(id, true) as id, username, password, enabled, "
            + "account_non_expired, account_non_locked, credentials_non_expired "
            + "FROM `user` WHERE id = UUID_TO_BIN(?, true)")) {
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

    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT BIN_TO_UUID(id, true) AS id, username, password, enabled, "
            + "account_non_expired, account_non_locked, credentials_non_expired "
            + "FROM `user`")) {

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
  public void delete(UUID id) {
    try (PreparedStatement ps = connection.prepareStatement(
        "DELETE FROM `user` WHERE id = UUID_TO_BIN(?, true)")) {
      ps.setString(1, id.toString());
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
