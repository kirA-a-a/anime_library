package io.student.rcc.data.rococoApi;

import io.student.rcc.data.rococoApi.entity.MuseumEntity;
import java.util.Optional;
import java.util.UUID;

public interface MuseumDao {

  MuseumEntity create(MuseumEntity entity);

  Optional<MuseumEntity> findById(UUID id);

  Optional<MuseumEntity> findByTitle(String title);

  MuseumEntity update(MuseumEntity entity);

  void delete(MuseumEntity entity);
}
