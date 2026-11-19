package io.student.rcc.model;

import io.student.rcc.data.rococoAuth.entity.AuthUserEntity;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public record AuthUserJson(
    @Nullable UUID id,
    String username,
    String password,
    boolean enabled,
    boolean accountNonExpired,
    boolean accountNonLocked,
    boolean credentialsNonExpired
) {

  public static @Nonnull AuthUserJson fromEntity(AuthUserEntity entity) {
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
