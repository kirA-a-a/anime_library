package io.student.rcc.model;

import io.student.rcc.data.rococoApi.entity.MuseumEntity;
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
        entity.getPhoto(),
        entity.getCountryId()
    );
  }
}
