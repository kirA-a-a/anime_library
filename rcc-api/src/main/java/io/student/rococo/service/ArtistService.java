package io.student.rococo.service;

import io.student.rococo.data.entity.ArtistEntity;
import io.student.rococo.data.repository.ArtistRepository;
import io.student.rococo.exception.ResourceNotFoundException;
import io.student.rococo.model.ArtistJson;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ArtistService {

  private final ArtistRepository artistRepository;

  @Autowired
  public ArtistService(ArtistRepository artistRepository) {
    this.artistRepository = artistRepository;
  }

  public ArtistJson getArtistById(String id) {
    ArtistEntity entity = artistRepository
        .findById(UUID.fromString(id))
        .orElseThrow(() -> new ResourceNotFoundException(
            String.format("Художник не найден по id: %s", id)));

    return new ArtistJson(
        entity.getId(),
        entity.getName(),
        entity.getBiography(),
        Arrays.toString(entity.getPhoto() == null ? null : entity.getPhoto()));
  }

  public Page<ArtistJson> getArtists(Pageable pageable) {
    return artistRepository.findAll(pageable)
        .map(e -> new ArtistJson(
            e.getId(),
            e.getName(),
            e.getBiography(),
            Arrays.toString(e.getPhoto() == null ? null : e.getPhoto())
        ));
  }

  public Page<ArtistJson> getArtistByName(Pageable pageable, String name) {
    return artistRepository.findByNameContainsIgnoreCase(name, pageable)
        .map(e -> new ArtistJson(
            e.getId(),
            e.getName(),
            e.getBiography(),
            Arrays.toString(e.getPhoto() == null ? null : e.getPhoto())
        ));
  }

  public ArtistJson saveArtist(ArtistJson artistJson) {

    ArtistEntity entity = new ArtistEntity();
    entity.setId(artistJson.id());
    entity.setName(artistJson.name());
    entity.setBiography(artistJson.biography());
    entity.setPhoto(artistJson.photo() != null ? artistJson.photo().getBytes(StandardCharsets.UTF_8) : null);

    ArtistEntity save = artistRepository.save(entity);

    return new ArtistJson(
        save.getId(),
        save.getName(),
        save.getBiography(),
        save.getPhoto() != null ? new String(save.getPhoto(), StandardCharsets.UTF_8) : null
    );
  }

  public ArtistJson updateArtist(ArtistJson artistJson) {

    Optional<ArtistEntity> entityOpt = artistRepository.findById(artistJson.id())
        .map(e -> {
          e.setName(artistJson.name() != null ? artistJson.name() : e.getName());
          e.setBiography(artistJson.biography() != null ? artistJson.biography() : e.getBiography());
          e.setPhoto(artistJson.photo() == null ? null : artistJson.photo().getBytes(StandardCharsets.UTF_8));
          return artistRepository.save(e);
        });

    ArtistEntity entity = entityOpt.orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    return new ArtistJson(
        entity.getId(),
        entity.getName(),
        entity.getBiography(),
        entity.getPhoto() != null ? new String(entity.getPhoto(), StandardCharsets.UTF_8) : null
    );
  }
}
