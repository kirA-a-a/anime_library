package io.student.rcc.model;

import io.student.rcc.data.rococoApi.entity.MuseumEntity;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public record MuseumJson(
    UUID id,
    String title,
    String description,
    String city,
    String photo,
    UUID countryId
) {

  public static MuseumJson fromEntity(MuseumEntity entity) {
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
