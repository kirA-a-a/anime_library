package io.student.rcc.model;

import io.student.rcc.data.rococoAuth.entity.AuthAuthorityEntity;
import io.student.rcc.data.rococoAuth.entity.Authority;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public record AuthAuthorityJson(
    @Nullable UUID userId,
    Authority authority
) {

  public static @Nonnull AuthAuthorityJson fromEntity(@Nonnull AuthAuthorityEntity entity) {
    return new AuthAuthorityJson(
        entity.getUser() != null ? entity.getUser().getId() : null,
        entity.getAuthority()
    );
  }
}
