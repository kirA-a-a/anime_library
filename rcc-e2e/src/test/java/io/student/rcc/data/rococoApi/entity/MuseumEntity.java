package io.student.rcc.data.rococoApi.entity;

import io.student.rcc.model.MuseumJson;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MuseumEntity {

  private UUID id;
  private String title;
  private String description;
  private String city;
  private String photo;
  private UUID countryId;

  public static MuseumEntity fromJson(MuseumJson json) {
    MuseumEntity entity = new MuseumEntity();
    entity.setId(json.id());
    entity.setTitle(json.title());
    entity.setDescription(json.description());
    entity.setCity(json.city());
    entity.setPhoto(json.photo());
    entity.setCountryId(json.countryId());
    return entity;
  }
}
