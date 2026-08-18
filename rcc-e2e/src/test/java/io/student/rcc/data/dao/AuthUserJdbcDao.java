package io.student.rcc.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
  public UUID createUser(UUID id, String username, String password) {
    try (PreparedStatement ps = connection.prepareStatement(
        "INSERT INTO `user` "
            + "(id, username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)"
            + " VALUES (UUID_TO_BIN(?, true), ?, ?, ?, ?, ?, ?)"
    )) {
      ps.setString(1, id.toString());
      ps.setString(2, username);
      ps.setString(3, passwordEncoder.encode(password));
      ps.setBoolean(4, true);
      ps.setBoolean(5, true);
      ps.setBoolean(6, true);
      ps.setBoolean(7, true);
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return id;
  }
}
