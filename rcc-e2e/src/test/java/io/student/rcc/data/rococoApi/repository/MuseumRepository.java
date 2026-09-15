package io.student.rcc.data.rococoApi.repository;

import io.student.rcc.data.rococoApi.entity.MuseumEntity;
import io.student.rcc.data.rococoApi.repository.iml.MuseumRepositoryHibernate;
import io.student.rcc.data.rococoApi.repository.iml.MuseumRepositoryJdbc;
import io.student.rcc.data.rococoApi.repository.iml.MuseumRepositorySpringJdbc;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MuseumRepository {

  static MuseumRepository getInstance() {
    return switch (System.getProperty("repository.impl", "jpa")) {
      case "jdbc" -> new MuseumRepositoryJdbc();
      case "spring-jdbc" -> new MuseumRepositorySpringJdbc();
      default -> new MuseumRepositoryHibernate();
    };
  }

  MuseumEntity create(MuseumEntity entity);

  Optional<MuseumEntity> findById(UUID id);

  List<MuseumEntity> findAll();

  Optional<MuseumEntity> findByTitle(String title);

  MuseumEntity update(MuseumEntity entity);

  void delete(MuseumEntity entity);
}
