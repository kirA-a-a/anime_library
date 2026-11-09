package io.student.rcc.api.core;

import io.student.rcc.jupiter.extension.ApiLoginExtension;
import java.io.IOException;
import java.util.Objects;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.apache.commons.lang.StringUtils;

public class CodeInterceptor implements Interceptor {

  @Override
  public Response intercept(Chain chain) throws IOException {
    final Response response = chain.proceed(chain.request());

    if (response.isRedirect()) {
      String location = Objects.requireNonNull(response.header("Location"));

      if (location.contains("code=")) {
        ApiLoginExtension.setCode(StringUtils.substringAfter(location, "code="));
      }

    }

    return response;
  }

}
