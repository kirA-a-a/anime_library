package io.student.rcc.api;

import com.fasterxml.jackson.databind.JsonNode;
import javax.annotation.ParametersAreNonnullByDefault;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

@ParametersAreNonnullByDefault
public interface AuthApi {

  @GET("/register")
  Call<ResponseBody> requestRegisterForm();

  @FormUrlEncoded
  @POST("/register")
  Call<Void> registerUser(
      @Field("_csrf") String csrf,
      @Field("username") String username,
      @Field("password") String password,
      @Field("passwordSubmit") String passwordSubmit
  );

  @GET("/oauth2/authorize")
  Call<Void> authorize(
      @Query("response_type") String responseType,
      @Query("client_id") String clientId,
      @Query("redirect_uri") String redirectUri,
      @Query("scope") String scope,
      @Query("code_challenge") String codeChallenge,
      @Query("code_challenge_method") String codeChallengeMethod
  );

  @GET("/login")
  Call<ResponseBody> requestLoginForm();

  @FormUrlEncoded
  @POST("/login")
  Call<Void> login(
      @Field("_csrf") String csrf,
      @Field("username") String username,
      @Field("password") String password
  );

  @GET
  Call<Void> followRedirect(@Url String url);

  @POST("/oauth2/token")
  @FormUrlEncoded
  Call<JsonNode> token(
      @Field("client_id") String clientId,
      @Field("redirect_uri") String redirectUri,
      @Field("grant_type") String grantType,
      @Field("code") String code,
      @Field("code_verifier") String codeVerifier
  );
}
