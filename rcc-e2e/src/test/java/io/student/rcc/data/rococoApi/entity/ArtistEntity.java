package io.student.rcc.data.rococoApi.entity;

import io.student.rcc.model.ArtistJson;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ArtistEntity {

  private UUID id;
  private String name;
  private String biography;
  private String photo;

  public static ArtistEntity fromJson(ArtistJson json) {
    ArtistEntity entity = new ArtistEntity();
    entity.setId(json.id());
    entity.setName(json.name());
    entity.setBiography(json.biography());
    entity.setPhoto(json.photo());
    return entity;
  }
}
