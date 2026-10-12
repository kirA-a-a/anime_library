package io.student.rcc.api;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

import io.qameta.allure.okhttp3.AllureOkHttp3;
import io.student.rcc.api.core.ThreadSafeCookieStore;
import io.student.rcc.config.Config;
import java.net.CookieManager;
import java.net.CookiePolicy;
import lombok.Getter;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.apache.commons.lang3.ArrayUtils;
import retrofit2.Converter;
import retrofit2.Converter.Factory;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public abstract class BaseRestClient {

  private final OkHttpClient okHttpClient;
  @Getter
  protected final Retrofit retrofit;
  protected static final Config CFG = Config.getInstance();

  public BaseRestClient(String baseUrl) {
    this(baseUrl, false, JacksonConverterFactory.create(), BODY);
  }

  public BaseRestClient(String baseUrl, boolean followRedirect) {
    this(baseUrl, followRedirect, JacksonConverterFactory.create(), BODY);
  }

  public BaseRestClient(String baseUrl, Converter.Factory factory) {
    this(baseUrl, false, factory, BODY);
  }

  public BaseRestClient(
      String baseUrl,
      boolean followRedirect,
      Converter.Factory factory,
      HttpLoggingInterceptor.Level level,
      Interceptor... interceptors
  ) {
    final OkHttpClient.Builder builder = new OkHttpClient.Builder()
        .followRedirects(followRedirect);

    if (ArrayUtils.isNotEmpty(interceptors)) {
      for (Interceptor interceptor : interceptors) {
        builder.addNetworkInterceptor(interceptor);
      }
    }

    builder.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(level));
    builder.cookieJar(
        new JavaNetCookieJar(
            new CookieManager(
                ThreadSafeCookieStore.INSTANCE,
                CookiePolicy.ACCEPT_ALL
            )
        )
    );
    builder.addNetworkInterceptor(new AllureOkHttp3());

    this.okHttpClient = builder.build();
    this.retrofit = new Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(factory)
        .build();
  }

  /**
   * Композиция.
   *
   * <p>Зачем это тут: если понадобится несколько разных REST-клиентов
   * (разный url, redirect, interceptor), их удобнее держать полями с разной конфигурацией, чем
   * строить длинную иерархию {@code extends}.
   *
   * <p>{@link EmptyRestClient} — минимальный готовый клиент без своих API-методов.
   * Его можно создать и положить полем в другой класс (это и есть композиция), а не обязательно от
   * него наследоваться.
   */
  public final class EmptyRestClient extends BaseRestClient {

    public EmptyRestClient(String baseUrl) {
      super(baseUrl);
    }

    public EmptyRestClient(String baseUrl, boolean followRedirect) {
      super(baseUrl, followRedirect);
    }

    public EmptyRestClient(String baseUrl, Factory factory) {
      super(baseUrl, factory);
    }

    public EmptyRestClient(String baseUrl, boolean followRedirect, Factory factory,
        Level level, Interceptor... interceptors) {
      super(baseUrl, followRedirect, factory, level, interceptors);
    }
  }
}
