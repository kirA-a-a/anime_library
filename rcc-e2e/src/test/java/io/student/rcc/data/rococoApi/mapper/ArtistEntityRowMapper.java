package io.student.rcc.data.rococoApi.mapper;

import io.student.rcc.data.rococoApi.entity.ArtistEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;

public class ArtistEntityRowMapper implements RowMapper<ArtistEntity> {

  public static final ArtistEntityRowMapper instance = new ArtistEntityRowMapper();

  private ArtistEntityRowMapper() {
  }

  @Override
  public @Nullable ArtistEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    ArtistEntity entity = new ArtistEntity();
    entity.setId(UUID.fromString(rs.getString("id")));
    entity.setName(rs.getString("name"));
    entity.setBiography(rs.getString("biography"));
    entity.setPhoto(rs.getBytes("photo"));
    return entity;
  }
}
