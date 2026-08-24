package io.student.rcc.model;

import io.student.rcc.data.rococoApi.entity.ArtistEntity;
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
        entity.getPhoto()
    );
  }
}
