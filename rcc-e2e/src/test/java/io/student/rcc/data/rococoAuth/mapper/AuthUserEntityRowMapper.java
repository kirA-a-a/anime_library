package io.student.rcc.data.rococoAuth.mapper;

import io.student.rcc.data.rococoAuth.entity.AuthUserEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;

public class AuthUserEntityRowMapper implements RowMapper<AuthUserEntity> {

  public static final AuthUserEntityRowMapper instance = new AuthUserEntityRowMapper();

  private AuthUserEntityRowMapper() {
  }

  @Override
  public @Nullable AuthUserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

    AuthUserEntity result = new AuthUserEntity();

    result.setId(UUID.fromString(rs.getString("id")));
    result.setUsername(rs.getString("username"));
    result.setPassword(rs.getString("password"));
    result.setEnabled(rs.getBoolean("enabled"));
    result.setAccountNonExpired(rs.getBoolean("account_non_expired"));
    result.setAccountNonLocked(rs.getBoolean("account_non_locked"));
    result.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));

    return result;
  }
}
