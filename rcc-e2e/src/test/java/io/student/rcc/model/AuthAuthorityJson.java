package io.student.rcc.model;

import io.student.rcc.data.dao.rococoAuth.entity.AuthAuthorityEntity;
import io.student.rcc.data.dao.rococoAuth.entity.AuthAuthorityEntity.Authority;
import java.util.UUID;

public record AuthAuthorityJson(
    UUID userId,
    Authority authority
) {

  public static AuthAuthorityJson fromEntity(AuthAuthorityEntity entity) {
    return new AuthAuthorityJson(
        entity.getUserId(),
        entity.getAuthority()
    );
  }
}
