package io.student.rcc.data.rococoApi.repository;

import io.student.rcc.data.rococoApi.entity.MuseumEntity;
import io.student.rcc.data.rococoApi.repository.iml.MuseumRepositoryHibernate;
import io.student.rcc.data.rococoApi.repository.iml.MuseumRepositoryJdbc;
import io.student.rcc.data.rococoApi.repository.iml.MuseumRepositorySpringJdbc;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface MuseumRepository {

  static @Nonnull MuseumRepository getInstance() {
    return switch (System.getProperty("repository.impl", "jpa")) {
      case "jdbc" -> new MuseumRepositoryJdbc();
      case "spring-jdbc" -> new MuseumRepositorySpringJdbc();
      default -> new MuseumRepositoryHibernate();
    };
  }

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
