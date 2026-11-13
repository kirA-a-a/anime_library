package io.student.rcc.test.graphql;

import com.apollographql.java.client.ApolloClient;
import io.qameta.allure.okhttp3.AllureOkHttp3;
import io.student.rcc.config.Config;
import io.student.rcc.jupiter.annotation.meta.GraphQlTest;
import io.student.rcc.jupiter.extension.ApiLoginExtension;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.junit.jupiter.api.extension.RegisterExtension;

@GraphQlTest
public class BaseGraphQlTest {

  @RegisterExtension
  protected static final ApiLoginExtension apiLoginExtension = ApiLoginExtension.rest();
  protected static final Config CFG = Config.getInstance();
  protected static final ApolloClient apolloClient = new ApolloClient.Builder()
      .serverUrl(CFG.apiUrl() + "graphql")
      .okHttpClient(
          new OkHttpClient.Builder()
              .addNetworkInterceptor(new AllureOkHttp3())
              .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(Level.BODY)).build()
      ).build();

}
