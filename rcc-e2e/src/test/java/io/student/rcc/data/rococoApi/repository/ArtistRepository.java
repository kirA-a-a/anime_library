package io.student.rcc.data.rococoApi.repository;

import io.student.rcc.data.rococoApi.entity.ArtistEntity;
import io.student.rcc.data.rococoApi.repository.iml.ArtistRepositoryHibernate;
import io.student.rcc.data.rococoApi.repository.iml.ArtistRepositoryJdbc;
import io.student.rcc.data.rococoApi.repository.iml.ArtistRepositorySpringJdbc;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ArtistRepository {

  static ArtistRepository getInstance() {
    return switch (System.getProperty("repository.impl", "jpa")) {
      case "jdbc" -> new ArtistRepositoryJdbc();
      case "spring-jdbc" -> new ArtistRepositorySpringJdbc();
      default -> new ArtistRepositoryHibernate();
    };
  }

  ArtistEntity create(ArtistEntity entity);

  Optional<ArtistEntity> findById(UUID id);

  List<ArtistEntity> findAll();

  Optional<ArtistEntity> findByName(String name);

  ArtistEntity update(ArtistEntity entity);

  void delete(ArtistEntity entity);
}
