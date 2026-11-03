package io.student.rcc.service;

import io.student.rcc.model.MuseumJson;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface MuseumsClient {

  @Nonnull
  MuseumJson create(MuseumJson museum);

  @Nonnull
  Optional<MuseumJson> findById(UUID id);

  @Nonnull
  Optional<MuseumJson> findByTitle(String title);

  @Nonnull
  List<MuseumJson> findAll();

  @Nonnull
  MuseumJson update(MuseumJson museum);

  void delete(MuseumJson museum);
}
