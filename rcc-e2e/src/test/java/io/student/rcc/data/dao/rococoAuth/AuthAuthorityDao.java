package io.student.rcc.data.dao.rococoAuth;

import io.student.rcc.data.dao.rococoAuth.entity.AuthAuthorityEntity;
import java.util.UUID;

public interface AuthAuthorityDao {

    AuthAuthorityEntity create(AuthAuthorityEntity entity);

    void deleteByUserId(UUID userId);

}
