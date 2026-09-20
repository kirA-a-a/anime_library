package io.student.rcc.model;

import io.student.rcc.data.rococoApi.entity.ArtistEntity;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public record ArtistJson(
    @Nullable UUID id,
    String name,
    String biography,
    @Nullable String photo
) {

  public static @Nonnull ArtistJson fromEntity(@Nonnull ArtistEntity entity) {
    return new ArtistJson(
        entity.getId(),
        entity.getName(),
        entity.getBiography(),
        entity.getPhoto() != null ? new String(entity.getPhoto(), StandardCharsets.UTF_8) : null
    );
  }
}
