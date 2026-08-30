package io.student.rcc.data.rococoApi.entity;

import io.student.rcc.model.PaintingJson;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PaintingEntity {

  private UUID id;
  private String title;
  private String description;
  private UUID artistId;
  private UUID museumId;
  private String content;

  public static PaintingEntity fromJson(PaintingJson json) {
    PaintingEntity entity = new PaintingEntity();
    entity.setId(json.id());
    entity.setTitle(json.title());
    entity.setDescription(json.description());
    entity.setArtistId(json.artistId());
    entity.setMuseumId(json.museumId());
    entity.setContent(json.content());
    return entity;
  }
}
