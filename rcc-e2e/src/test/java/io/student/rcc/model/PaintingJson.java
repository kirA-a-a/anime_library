package io.student.rcc.model;

import io.student.rcc.data.rococoApi.entity.PaintingEntity;
import java.util.UUID;

public record PaintingJson(
    UUID id,
    String title,
    String description,
    String content,
    UUID artistId,
    UUID museumId
) {

  public static PaintingJson fromEntity(PaintingEntity entity) {
    return new PaintingJson(
        entity.getId(),
        entity.getTitle(),
        entity.getDescription(),
        entity.getContent(),
        entity.getArtistId(),
        entity.getMuseumId()
    );
  }
}
