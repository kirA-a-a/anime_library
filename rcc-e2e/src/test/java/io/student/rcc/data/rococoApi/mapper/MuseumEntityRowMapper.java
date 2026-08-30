package io.student.rcc.data.rococoApi.mapper;

import io.student.rcc.data.rococoApi.entity.MuseumEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;

public class MuseumEntityRowMapper implements RowMapper<MuseumEntity> {

  public static final MuseumEntityRowMapper instance = new MuseumEntityRowMapper();

  private MuseumEntityRowMapper() {
  }

  @Override
  public @Nullable MuseumEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    MuseumEntity entity = new MuseumEntity();
    entity.setId(UUID.fromString(rs.getString("id")));
    entity.setTitle(rs.getString("title"));
    entity.setDescription(rs.getString("description"));
    entity.setCity(rs.getString("city"));
    byte[] photo = rs.getBytes("photo");
    entity.setPhoto(photo != null ? new String(photo) : null);
    entity.setCountryId(UUID.fromString(rs.getString("country_id")));
    return entity;
  }
}
