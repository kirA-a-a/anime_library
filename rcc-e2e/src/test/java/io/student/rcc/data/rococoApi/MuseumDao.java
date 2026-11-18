package io.student.rcc.data.rococoApi;

import io.student.rcc.data.rococoApi.entity.MuseumEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface MuseumDao {

  @Nonnull
  MuseumEntity create(MuseumEntity entity);

  @Nonnull
  Optional<MuseumEntity> findById(UUID id);

  @Nonnull
  List<MuseumEntity> findAll();

  @Nonnull
  Optional<MuseumEntity> findByTitle(String title);

  @Nonnull
  MuseumEntity update(MuseumEntity entity);

  void delete(MuseumEntity entity);
}
