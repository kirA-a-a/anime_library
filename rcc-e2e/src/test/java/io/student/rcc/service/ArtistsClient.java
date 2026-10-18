package io.student.rcc.service;

import io.student.rcc.model.ArtistJson;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface ArtistsClient {

  @Nonnull
  ArtistJson create(ArtistJson artist);

  @Nonnull
  Optional<ArtistJson> findById(UUID id);

  @Nonnull
  Optional<ArtistJson> findByName(String name);

  @Nonnull
  List<ArtistJson> findAll();

  @Nonnull
  ArtistJson update(ArtistJson artist);

  void delete(ArtistJson artist);
}
