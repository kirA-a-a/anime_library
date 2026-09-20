package io.student.rcc.model;

import io.student.rcc.data.rococoApi.entity.PaintingEntity;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public record PaintingJson(
    @Nullable UUID id,
    String title,
    String description,
    @Nullable String content,
    @Nullable UUID artistId,
    @Nullable UUID museumId
) {

  public static @Nonnull PaintingJson fromEntity(@Nonnull PaintingEntity entity) {
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
