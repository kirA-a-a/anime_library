package io.student.rcc.data.rococoApi.mapper;

import io.student.rcc.data.rococoApi.entity.CountryEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;

public class CountryEntityRowMapper implements RowMapper<CountryEntity> {

  public static final CountryEntityRowMapper instance = new CountryEntityRowMapper();

  private CountryEntityRowMapper() {
  }

  @Override
  public @Nullable CountryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    CountryEntity entity = new CountryEntity();
    entity.setId(UUID.fromString(rs.getString("id")));
    entity.setName(rs.getString("name"));
    return entity;
  }
}
