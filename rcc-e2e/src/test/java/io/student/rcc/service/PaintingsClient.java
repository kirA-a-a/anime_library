package io.student.rcc.service;

import io.student.rcc.model.PaintingJson;
import java.util.Optional;
import java.util.UUID;

public interface PaintingsClient {

  PaintingJson create(PaintingJson painting);

  Optional<PaintingJson> findById(UUID id);

  Optional<PaintingJson> findByTitle(String title);

  void delete(PaintingJson painting);
}
