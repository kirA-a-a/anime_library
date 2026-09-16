package io.student.rcc.model;

import io.student.rcc.data.rococoAuth.entity.AuthAuthorityEntity;
import io.student.rcc.data.rococoAuth.entity.Authority;
import java.util.UUID;

public record AuthAuthorityJson(
    UUID userId,
    Authority authority
) {

  public static AuthAuthorityJson fromEntity(AuthAuthorityEntity entity) {
    return new AuthAuthorityJson(
        entity.getUser() != null ? entity.getUser().getId() : null,
        entity.getAuthority()
    );
  }
}
