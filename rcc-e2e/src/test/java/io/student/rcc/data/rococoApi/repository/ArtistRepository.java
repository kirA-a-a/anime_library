package io.student.rcc.data.rococoApi.repository;

import io.student.rcc.data.rococoApi.entity.ArtistEntity;
import io.student.rcc.data.rococoApi.repository.iml.ArtistRepositoryHibernate;
import io.student.rcc.data.rococoApi.repository.iml.ArtistRepositoryJdbc;
import io.student.rcc.data.rococoApi.repository.iml.ArtistRepositorySpringJdbc;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface ArtistRepository {

  static @Nonnull ArtistRepository getInstance() {
    return switch (System.getProperty("repository.impl", "jpa")) {
      case "jdbc" -> new ArtistRepositoryJdbc();
      case "spring-jdbc" -> new ArtistRepositorySpringJdbc();
      default -> new ArtistRepositoryHibernate();
    };
  }

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
