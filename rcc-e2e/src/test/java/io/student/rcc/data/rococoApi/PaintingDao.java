package io.student.rcc.data.rococoApi;

import io.student.rcc.data.rococoApi.entity.PaintingEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface PaintingDao {

  @Nonnull
  PaintingEntity create(PaintingEntity entity);

  @Nonnull
  Optional<PaintingEntity> findById(UUID id);

  @Nonnull
  List<PaintingEntity> findAll();

  @Nonnull
  Optional<PaintingEntity> findByTitle(String title);

  @Nonnull
  PaintingEntity update(PaintingEntity entity);

  void delete(PaintingEntity entity);
}
