package io.student.rococo.data.repository;

import io.student.rococo.data.entity.MuseumEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaintingRepository extends JpaRepository<MuseumEntity, UUID> {

}
