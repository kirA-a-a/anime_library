package io.student.rcc.data.rococoApi.mapper;

import io.student.rcc.data.rococoApi.entity.ArtistEntity;
import io.student.rcc.data.rococoApi.entity.PaintingEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class PaintingWithArtistExtractor implements ResultSetExtractor<List<PaintingEntity>> {

  public static final PaintingWithArtistExtractor instance = new PaintingWithArtistExtractor();

  private PaintingWithArtistExtractor() {
  }

  @Override
  public @Nullable List<PaintingEntity> extractData(ResultSet rs)
      throws SQLException, DataAccessException {

    LinkedHashMap<UUID, PaintingEntity> paintingMap = new LinkedHashMap<>();

    while (rs.next()) {
      UUID paintingId = UUID.fromString(rs.getString("id"));
      PaintingEntity paint = PaintingEntityRowMapper.instance.mapRow(rs, 1);
      PaintingEntity painting = paintingMap.computeIfAbsent(paintingId, id -> paint);

      String artistId = rs.getString("artist_id");

      if (artistId != null) {
        ArtistEntity artist = new ArtistEntity();
        artist.setId(UUID.fromString(artistId));
        artist.setName(rs.getString("artist_name"));
        artist.setBiography(rs.getString("artist_biography"));
        artist.setPhoto(rs.getBytes("artist_photo"));
        painting.setArtist(artist);
      }

    }

    return new ArrayList<>(paintingMap.values());
  }
}
