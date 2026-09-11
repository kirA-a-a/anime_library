package io.student.rcc.model;

import io.student.rcc.data.rococoApi.entity.CountryEntity;
import java.util.UUID;

public record CountryJson(
    UUID id,
    String name
) {

  public static CountryJson fromEntity(CountryEntity entity) {
    return new CountryJson(
        entity.getId(),
        entity.getName()
    );
  }
}
