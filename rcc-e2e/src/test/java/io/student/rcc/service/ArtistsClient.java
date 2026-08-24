package io.student.rcc.service;

import io.student.rcc.model.ArtistJson;
import java.util.Optional;
import java.util.UUID;

public interface ArtistsClient {

  ArtistJson create(ArtistJson artist);

  Optional<ArtistJson> findById(UUID id);

  Optional<ArtistJson> findByName(String name);

  void delete(ArtistJson artist);
}
