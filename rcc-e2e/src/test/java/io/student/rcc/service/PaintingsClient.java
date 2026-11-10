package io.student.rcc.service;

import io.student.rcc.model.PaintingJson;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface PaintingsClient {

  @Nonnull
  PaintingJson create(PaintingJson painting);

  @Nonnull
  Optional<PaintingJson> findById(UUID id);

  @Nonnull
  Optional<PaintingJson> findByTitle(String title);

  @Nonnull
  List<PaintingJson> findAll();

  @Nonnull
  PaintingJson update(PaintingJson painting);

  void delete(PaintingJson painting);
}
