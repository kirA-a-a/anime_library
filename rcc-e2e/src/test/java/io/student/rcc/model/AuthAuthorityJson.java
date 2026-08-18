package io.student.rcc.model;

import io.student.rcc.data.entity.AuthAuthorityEntity;
import java.util.UUID;

public record AuthAuthorityJson(
    UUID userId,
    String authority
) {

  public static AuthAuthorityJson fromEntity(AuthAuthorityEntity entity) {
    return new AuthAuthorityJson(
        entity.getUserId(),
        entity.getAuthority()
    );
  }
}
