package io.student.rcc.data.rococoApi.repository;

import io.student.rcc.data.rococoApi.entity.PaintingEntity;
import io.student.rcc.data.rococoApi.repository.iml.PaintingRepositoryHibernate;
import io.student.rcc.data.rococoApi.repository.iml.PaintingRepositoryJdbc;
import io.student.rcc.data.rococoApi.repository.iml.PaintingRepositorySpringJdbc;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaintingRepository {

  static PaintingRepository getInstance() {
    return switch (System.getProperty("repository.impl", "jpa")) {
      case "jdbc" -> new PaintingRepositoryJdbc();
      case "spring-jdbc" -> new PaintingRepositorySpringJdbc();
      default -> new PaintingRepositoryHibernate();
    };
  }

  PaintingEntity create(PaintingEntity entity);

  Optional<PaintingEntity> findById(UUID id);

  List<PaintingEntity> findAll();

  Optional<PaintingEntity> findByTitle(String title);

  PaintingEntity update(PaintingEntity entity);

  void delete(PaintingEntity entity);
}
