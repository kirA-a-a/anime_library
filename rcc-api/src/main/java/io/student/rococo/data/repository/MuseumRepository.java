package io.student.rococo.data.repository;

import io.student.rococo.data.entity.MuseumEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MuseumRepository extends JpaRepository<MuseumEntity, UUID> {

}
