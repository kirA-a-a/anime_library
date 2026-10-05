package io.student.rococo.data.repository;

import io.student.rococo.data.entity.ArtistEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArtistRepository extends JpaRepository<ArtistEntity, UUID> {

  @Query(value = "select * from artist where id = uuid_to_bin(:id, true)", nativeQuery = true)
  Optional<ArtistEntity> findByIdSwapped(String id);

  Page<ArtistEntity> findByNameContainsIgnoreCase(String name, Pageable pageable);

}
