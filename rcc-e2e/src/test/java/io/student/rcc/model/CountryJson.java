package io.student.rcc.model;

import io.student.rcc.data.rococoApi.entity.CountryEntity;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public record CountryJson(
    @Nullable UUID id,
    String name
) {

  public static @Nonnull CountryJson fromEntity(@Nonnull CountryEntity entity) {
    return new CountryJson(
        entity.getId(),
        entity.getName()
    );
  }
}
