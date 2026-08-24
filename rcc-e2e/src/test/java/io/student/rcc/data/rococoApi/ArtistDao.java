package io.student.rcc.data.rococoApi;

import io.student.rcc.data.rococoApi.entity.ArtistEntity;
import java.util.Optional;
import java.util.UUID;

public interface ArtistDao {

  ArtistEntity create(ArtistEntity entity);

  Optional<ArtistEntity> findById(UUID id);

  Optional<ArtistEntity> findByName(String name);

  ArtistEntity update(ArtistEntity entity);

  void delete(ArtistEntity entity);
}
