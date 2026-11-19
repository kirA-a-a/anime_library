package io.student.rcc.data.rococoApi;

import io.student.rcc.data.rococoApi.entity.ArtistEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface ArtistDao {

  @Nonnull
  ArtistEntity create(ArtistEntity entity);

  @Nonnull
  Optional<ArtistEntity> findById(UUID id);

  @Nonnull
  List<ArtistEntity> findAll();

  @Nonnull
  Optional<ArtistEntity> findByName(String name);

  @Nonnull
  ArtistEntity update(ArtistEntity entity);

  void delete(ArtistEntity entity);
}
