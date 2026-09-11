package io.student.rcc.data.rococoAuth;

import io.student.rcc.data.rococoAuth.entity.AuthAuthorityEntity;
import java.util.List;
import java.util.UUID;

public interface AuthAuthorityDao {

  void create(AuthAuthorityEntity... entities);

  List<AuthAuthorityEntity> findAll();

  void deleteByUserId(UUID userId);

}
