package io.student.rcc.data.rococoAuth.mapper;

import io.student.rcc.data.rococoAuth.entity.AuthAuthorityEntity;
import io.student.rcc.data.rococoAuth.entity.AuthUserEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class AuthUserWithAuthoritiesExtractor implements ResultSetExtractor<List<AuthUserEntity>> {

  public static final AuthUserWithAuthoritiesExtractor instance =
      new AuthUserWithAuthoritiesExtractor();

  private AuthUserWithAuthoritiesExtractor() {
  }

  @Override
  public @Nullable List<AuthUserEntity> extractData(ResultSet rs)
      throws SQLException, DataAccessException {

    LinkedHashMap<UUID, AuthUserEntity> userMap = new LinkedHashMap<>();

    while (rs.next()) {
      UUID userId = UUID.fromString(rs.getString("id"));
      AuthUserEntity candidate = AuthUserEntityRowMapper.instance.mapRow(rs, 1);
      AuthUserEntity user = userMap.computeIfAbsent(userId, id -> candidate);

      String authorityId = rs.getString("authority_id");
      if (authorityId != null) {
        AuthAuthorityEntity authority = new AuthAuthorityEntity();
        authority.setId(UUID.fromString(authorityId));
        authority.setUserId(UUID.fromString(rs.getString("user_id")));
        authority.setAuthority(
            AuthAuthorityEntity.Authority.valueOf(
                rs.getString("authority").toUpperCase()
            )
        );
        user.getAuthorities().add(authority);
      }
    }

    return new ArrayList<>(userMap.values());
  }
}
