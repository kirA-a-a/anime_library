package io.student.rcc.model;

import io.student.rcc.data.rococoAuth.entity.UserEntity;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public record UserJson(
    @Nullable UUID id,
    String username,
    @Nullable String firstname,
    @Nullable String lastname,
    @Nullable String avatar
) {

  public static @Nonnull UserJson fromEntity(UserEntity entity) {
    return new UserJson(
        entity.getId(),
        entity.getUsername(),
        entity.getFirstname(),
        entity.getLastname(),
        entity.getAvatar()
    );
  }
}
