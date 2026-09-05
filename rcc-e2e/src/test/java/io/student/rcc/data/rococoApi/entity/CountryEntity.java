package io.student.rcc.data.rococoApi.entity;

import io.student.rcc.model.CountryJson;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CountryEntity {

  private UUID id;
  private String name;

  public static CountryEntity fromJson(CountryJson json) {
    CountryEntity entity = new CountryEntity();
    entity.setId(json.id());
    entity.setName(json.name());
    return entity;
  }
}
