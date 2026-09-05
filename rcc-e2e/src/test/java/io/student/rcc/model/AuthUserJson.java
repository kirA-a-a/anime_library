package io.student.rcc.model;

import io.student.rcc.data.dao.rococoAuth.entity.AuthUserEntity;
import java.util.UUID;

public record AuthUserJson(
    UUID id,
    String username,
    String password,
    boolean enabled,
    boolean accountNonExpired,
    boolean accountNonLocked,
    boolean credentialsNonExpired
) {

  public static AuthUserJson fromEntity(AuthUserEntity entity) {
    return new AuthUserJson(
        entity.getId(),
        entity.getUsername(),
        entity.getPassword(),
        entity.isEnabled(),
        entity.isAccountNonExpired(),
        entity.isAccountNonLocked(),
        entity.isCredentialsNonExpired()
    );
  }
}
