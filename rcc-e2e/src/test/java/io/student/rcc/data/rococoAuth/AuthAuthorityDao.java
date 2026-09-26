package io.student.rcc.data.rococoAuth;

import io.student.rcc.data.rococoAuth.entity.AuthAuthorityEntity;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface AuthAuthorityDao {

  void create(AuthAuthorityEntity... entities);

  @Nonnull
  List<AuthAuthorityEntity> findAll();

  void deleteByUserId(UUID userId);

}
