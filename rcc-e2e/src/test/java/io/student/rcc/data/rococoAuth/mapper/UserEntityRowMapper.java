package io.student.rcc.data.rococoAuth.mapper;

import io.student.rcc.data.rococoAuth.entity.UserEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;

public class UserEntityRowMapper implements RowMapper<UserEntity> {

  public static final UserEntityRowMapper instance = new UserEntityRowMapper();

  private UserEntityRowMapper() {
  }

  @Override
  public @Nullable UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

    UserEntity result = new UserEntity();

    result.setId(rs.getObject("id", UUID.class));
    result.setUsername(rs.getString("username"));
    result.setFirstname(rs.getString("firstname"));
    result.setLastname(rs.getString("lastname"));
    result.setAvatar(rs.getString("avatar"));

    return result;
  }
}
