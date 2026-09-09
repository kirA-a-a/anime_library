package io.student.rcc.data.rococoAuth.mapper;

import io.student.rcc.data.rococoAuth.entity.AuthAuthorityEntity;
import io.student.rcc.data.rococoAuth.entity.AuthUserEntity;
import io.student.rcc.data.rococoAuth.entity.Authority;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;

public class AuthAuthorityEntityRowMapper implements RowMapper<AuthAuthorityEntity> {

  public static final AuthAuthorityEntityRowMapper instance = new AuthAuthorityEntityRowMapper();

  private AuthAuthorityEntityRowMapper() {
  }

  @Override
  public AuthAuthorityEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    AuthAuthorityEntity entity = new AuthAuthorityEntity();
    entity.setId(UUID.fromString(rs.getString("id")));

    AuthUserEntity user = new AuthUserEntity();
    user.setId(UUID.fromString(rs.getString("user_id")));
    entity.setUser(user);
    entity.setAuthority(Authority.valueOf(rs.getString("authority")));

    return entity;
  }
}
