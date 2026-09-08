package io.student.rcc.model;

import io.student.rcc.data.rococoApi.entity.PaintingEntity;
import java.nio.charset.StandardCharsets;
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
        entity.getContent() != null ? new String(entity.getContent(), StandardCharsets.UTF_8) : null,
        entity.getArtist() != null ? entity.getArtist().getId() : null,
        entity.getMuseum() != null ? entity.getMuseum().getId() : null
    );
  }
}
