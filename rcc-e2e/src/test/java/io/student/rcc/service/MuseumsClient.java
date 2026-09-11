package io.student.rcc.service;

import io.student.rcc.model.MuseumJson;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MuseumsClient {

  MuseumJson create(MuseumJson museum);

  Optional<MuseumJson> findById(UUID id);

  Optional<MuseumJson> findByTitle(String title);

  List<MuseumJson> findAll();

  MuseumJson update(MuseumJson museum);

  void delete(MuseumJson museum);
}
