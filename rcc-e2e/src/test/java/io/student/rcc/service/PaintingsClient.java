package io.student.rcc.service;

import io.student.rcc.model.PaintingJson;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaintingsClient {

  PaintingJson create(PaintingJson painting);

  Optional<PaintingJson> findById(UUID id);

  Optional<PaintingJson> findByTitle(String title);

  List<PaintingJson> findAll();

  PaintingJson update(PaintingJson painting);

  void delete(PaintingJson painting);
}
