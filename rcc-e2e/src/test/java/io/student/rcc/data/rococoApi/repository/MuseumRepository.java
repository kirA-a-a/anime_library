package io.student.rcc.data.rococoApi.repository;

import io.student.rcc.data.rococoApi.entity.MuseumEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MuseumRepository {

  MuseumEntity create(MuseumEntity entity);

  Optional<MuseumEntity> findById(UUID id);

  List<MuseumEntity> findAll();

  Optional<MuseumEntity> findByTitle(String title);

  MuseumEntity update(MuseumEntity entity);

  void delete(MuseumEntity entity);
}
