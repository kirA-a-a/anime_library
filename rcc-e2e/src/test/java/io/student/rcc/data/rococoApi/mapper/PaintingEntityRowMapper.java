package io.student.rcc.data.rococoApi.mapper;

import io.student.rcc.data.rococoApi.entity.ArtistEntity;
import io.student.rcc.data.rococoApi.entity.MuseumEntity;
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
    entity.setContent(rs.getBytes("content"));

    ArtistEntity artist = new ArtistEntity();
    artist.setId(UUID.fromString(rs.getString("artist_id")));
    entity.setArtist(artist);

    String museumId = rs.getString("museum_id");
    if (museumId != null) {
      MuseumEntity museum = new MuseumEntity();
      museum.setId(UUID.fromString(museumId));
      entity.setMuseum(museum);
    }
    return entity;
  }
}
