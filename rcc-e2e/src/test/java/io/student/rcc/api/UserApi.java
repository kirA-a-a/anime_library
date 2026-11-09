package io.student.rcc.api;

import io.student.rcc.model.UserJson;
import retrofit2.Call;
import retrofit2.http.GET;

public interface UserApi {

  /**
   * TODO: пока не работает в АТ как надо.
   * GET /api/user требует Authorization: Bearer JWT; без токена → 401.
   * Плюс UserController#getUser на бэке — заглушка (рандомный id).
   */
  @GET("/api/user")
  Call<UserJson> getUser();

}
