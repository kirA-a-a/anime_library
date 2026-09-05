package io.student.rcc.model;

import io.student.rcc.data.rococoApi.entity.ArtistEntity;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public record ArtistJson(
    UUID id,
    String name,
    String biography,
    String photo
) {

  public static ArtistJson fromEntity(ArtistEntity entity) {
    return new ArtistJson(
        entity.getId(),
        entity.getName(),
        entity.getBiography(),
        entity.getPhoto() != null ? new String(entity.getPhoto(), StandardCharsets.UTF_8) : null
    );
  }
}
