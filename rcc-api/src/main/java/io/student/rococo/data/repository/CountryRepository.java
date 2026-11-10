package io.student.rococo.data.repository;

import io.student.rococo.data.entity.CountryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CountryRepository extends JpaRepository<CountryEntity, UUID> {
  Page<CountryEntity> findAllByNameContainsIgnoreCase(String name, Pageable pageable);

  Optional<CountryEntity> findByName(String name);

  /**
   * Поиск по id с учётом MySQL {@code UUID_TO_BIN(..., true)} (swap).
   * Обычный {@code findById(UUID)} часто не находит строку, хотя UUID виден в IDE.
   */
  @Query(value = "select * from country where id = uuid_to_bin(:id, true)", nativeQuery = true)
  Optional<CountryEntity> findByIdSwapped(String id);
}
