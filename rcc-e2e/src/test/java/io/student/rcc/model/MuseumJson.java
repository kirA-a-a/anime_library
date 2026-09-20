package io.student.rcc.model;

import io.student.rcc.data.rococoApi.entity.MuseumEntity;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public record MuseumJson(
    @Nullable UUID id,
    String title,
    String description,
    String city,
    @Nullable String photo,
    @Nullable UUID countryId
) {

  public static @Nonnull MuseumJson fromEntity(@Nonnull MuseumEntity entity) {
    return new MuseumJson(
        entity.getId(),
        entity.getTitle(),
        entity.getDescription(),
        entity.getCity(),
        entity.getPhoto() != null ? new String(entity.getPhoto(), StandardCharsets.UTF_8) : null,
        entity.getCountry() != null ? entity.getCountry().getId() : null
    );
  }
}
