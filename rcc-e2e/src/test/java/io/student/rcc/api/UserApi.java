package io.student.rcc.api;

import io.student.rcc.model.UserJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;

public interface UserApi {

  @GET("/api/user")
  Call<UserJson> getUser(@Header("Authorization") String bearerToken);

  @PATCH("/api/user")
  Call<UserJson> updateUser(
      @Header("Authorization") String bearerToken,
      @Body UserJson user
  );
}
