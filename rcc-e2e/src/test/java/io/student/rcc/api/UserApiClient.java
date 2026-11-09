package io.student.rcc.api;

import io.student.rcc.model.UserJson;
import java.io.IOException;
import retrofit2.Response;

public class UserApiClient extends BaseRestClient {

  private final UserApi userApi;

  public UserApiClient() {
    super(CFG.apiUrl());
    this.userApi = retrofit.create(UserApi.class);
  }

  /**
   * TODO: сейчас всегда возвращает {@code null}.
   * Причина: запрос уходит без Bearer JWT → GET /api/user даёт 401 →
   * {@code isSuccessful() == false} → return null.
   * Нужен token (header/interceptor). Даже с JWT бэкенд UserController — stub.
   */
  public UserJson getUser() {
    try {
      // TODO: без Authorization сейчас всегда null (401)
      Response<UserJson> response = userApi.getUser().execute();
      return response.isSuccessful() ? response.body() : null;
    } catch (IOException e) {
      throw new AssertionError(e);
    }
  }
}
