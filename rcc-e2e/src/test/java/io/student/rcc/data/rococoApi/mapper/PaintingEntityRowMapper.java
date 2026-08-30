package io.student.rcc.data.rococoApi.mapper;

import io.student.rcc.data.rococoApi.entity.PaintingEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;

public class PaintingEntityRowMapper implements RowMapper<PaintingEntity> {

  public static final PaintingEntityRowMapper instance = new PaintingEntityRowMapper();

  private PaintingEntityRowMapper() {
  }

  @Override
  public @Nullable PaintingEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    PaintingEntity entity = new PaintingEntity();
    entity.setId(UUID.fromString(rs.getString("id")));
    entity.setTitle(rs.getString("title"));
    entity.setDescription(rs.getString("description"));
    entity.setArtistId(UUID.fromString(rs.getString("artist_id")));
    String museumId = rs.getString("museum_id");
    entity.setMuseumId(museumId != null ? UUID.fromString(museumId) : null);
    byte[] content = rs.getBytes("content");
    entity.setContent(content != null ? new String(content) : null);
    return entity;
  }
}
