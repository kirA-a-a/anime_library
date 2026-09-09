package io.student.rcc.data.rococoAuth.repository.iml;

import static io.student.rcc.data.tpl.Connections.holder;

import io.student.rcc.config.Config;
import io.student.rcc.data.rococoAuth.entity.AuthAuthorityEntity;
import io.student.rcc.data.rococoAuth.entity.AuthUserEntity;
import io.student.rcc.data.rococoAuth.entity.Authority;
import io.student.rcc.data.rococoAuth.mapper.AuthUserEntityRowMapper;
import io.student.rcc.data.rococoAuth.repository.AuthUserRepository;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthUserRepositoryJdbc implements AuthUserRepository {

  private static final Config CFG = Config.getInstance();
  private final PasswordEncoder passwordEncoder =
      PasswordEncoderFactories.createDelegatingPasswordEncoder();

  private static final String USER_WITH_AUTHORITIES_SELECT =
      "SELECT BIN_TO_UUID(u.id, true) AS id, u.username, u.password, u.enabled, "
          + "u.account_non_expired, u.account_non_locked, u.credentials_non_expired, "
          + "BIN_TO_UUID(a.id, true) AS authority_id, "
          + "BIN_TO_UUID(a.user_id, true) AS user_id, "
          + "a.authority AS authority "
          + "FROM `user` u "
          + "LEFT JOIN `authority` a ON u.id = a.user_id ";

  @Override
  public AuthUserEntity create(AuthUserEntity user) {
    try {
      if (user.getId() == null) {
        user.setId(UUID.randomUUID());
      }

      try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
          "INSERT INTO `user` "
              + "(id, username, password, enabled, account_non_expired, account_non_locked, "
              + "credentials_non_expired) "
              + "VALUES (UUID_TO_BIN(?, true), ?, ?, ?, ?, ?, ?)"
      )) {
        ps.setString(1, user.getId().toString());
        ps.setString(2, user.getUsername());
        ps.setString(3, passwordEncoder.encode(user.getPassword()));
        ps.setBoolean(4, user.isEnabled());
        ps.setBoolean(5, user.isAccountNonExpired());
        ps.setBoolean(6, user.isAccountNonLocked());
        ps.setBoolean(7, user.isCredentialsNonExpired());
        ps.executeUpdate();
      }

      if (user.getAuthorities() != null) {
        for (AuthAuthorityEntity authority : user.getAuthorities()) {
          if (authority.getId() == null) {
            authority.setId(UUID.randomUUID());
          }
          authority.setUser(user);
          try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
              "INSERT INTO `authority`(id, user_id, authority) "
                  + "VALUES (UUID_TO_BIN(?, true), UUID_TO_BIN(?, true), ?)"
          )) {
            ps.setString(1, authority.getId().toString());
            ps.setString(2, authority.getUser().getId().toString());
            ps.setString(3, authority.getAuthority().name());
            ps.executeUpdate();
          }
        }
      }

      return user;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<AuthUserEntity> findById(UUID id) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        USER_WITH_AUTHORITIES_SELECT + "WHERE u.id = UUID_TO_BIN(?, true)"
    )) {
      ps.setString(1, id.toString());

      try (ResultSet rs = ps.executeQuery()) {
        AuthUserEntity user = null;
        while (rs.next()) {
          if (user == null) {
            user = AuthUserEntityRowMapper.instance.mapRow(rs, 1);
            user.setAuthorities(new ArrayList<>());
          }
          addAuthorityIfPresent(user, rs);
        }
        return Optional.ofNullable(user);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<AuthUserEntity> findAll() {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        USER_WITH_AUTHORITIES_SELECT
    )) {
      try (ResultSet rs = ps.executeQuery()) {
        Map<UUID, AuthUserEntity> users = new LinkedHashMap<>();
        while (rs.next()) {
          UUID userId = UUID.fromString(rs.getString("id"));
          AuthUserEntity user = users.get(userId);
          if (user == null) {
            user = AuthUserEntityRowMapper.instance.mapRow(rs, users.size() + 1);
            user.setAuthorities(new ArrayList<>());
            users.put(userId, user);
          }
          addAuthorityIfPresent(user, rs);
        }
        return new ArrayList<>(users.values());
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(UUID id) {
    try {
      try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
          "DELETE FROM `authority` WHERE user_id = UUID_TO_BIN(?, true)"
      )) {
        ps.setString(1, id.toString());
        ps.executeUpdate();
      }
      try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
          "DELETE FROM `user` WHERE id = UUID_TO_BIN(?, true)"
      )) {
        ps.setString(1, id.toString());
        ps.executeUpdate();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private static void addAuthorityIfPresent(AuthUserEntity user, ResultSet rs) throws SQLException {
    String authorityId = rs.getString("authority_id");
    if (authorityId == null) {
      return;
    }
    AuthAuthorityEntity authority = new AuthAuthorityEntity();
    authority.setId(UUID.fromString(authorityId));
    authority.setUser(user);
    authority.setAuthority(Authority.valueOf(rs.getString("authority")));
    user.getAuthorities().add(authority);
  }
}
