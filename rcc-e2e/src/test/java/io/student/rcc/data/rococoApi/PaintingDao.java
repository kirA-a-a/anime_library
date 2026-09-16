package io.student.rcc.data.rococoApi;

import io.student.rcc.data.rococoApi.entity.PaintingEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaintingDao {

  PaintingEntity create(PaintingEntity entity);

  Optional<PaintingEntity> findById(UUID id);

  List<PaintingEntity> findAll();

  Optional<PaintingEntity> findByTitle(String title);

  PaintingEntity update(PaintingEntity entity);

  void delete(PaintingEntity entity);
}
