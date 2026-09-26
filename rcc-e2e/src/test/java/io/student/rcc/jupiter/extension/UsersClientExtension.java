package io.student.rcc.jupiter.extension;

import io.student.rcc.service.UsersApiClient;
import io.student.rcc.service.UsersClient;
import io.student.rcc.service.UsersDbClient;
import java.lang.reflect.Field;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

/**
 * Инжектит {@link UsersClient} в поля тестового класса.
 *
 * <p>{@link TestInstancePostProcessor} — JUnit 5 API: после создания экземпляра
 * тестового класса JUnit вызывает {@link #postProcessTestInstance} и даёт
 * возможность его настроить (подставить клиент в поле).
 *
 * <p>Если на тесте стоит {@code @ExtendWith(UsersClientExtension.class)}
 * (как в {@code JdbcTest}), перед каждым тестовым методом JUnit создаёт объект
 * теста и вызывает этот extension.
 *
 * <p>{@code testInstance} — сам объект тестового класса, схематично:
 * <pre>{@code
 * JdbcTest testInstance = new JdbcTest();
 * }</pre>
 */
public class UsersClientExtension implements TestInstancePostProcessor {

  @Override
  public void postProcessTestInstance(Object testInstance, ExtensionContext context)
      throws Exception {
    for (Field field : testInstance.getClass().getDeclaredFields()) {
      if (field.getType().isAssignableFrom(UsersClient.class)) {
        field.setAccessible(true);
        field.set(testInstance, resolve());
      }
    }
  }

  private UsersClient resolve() {
    return "api".equals(System.getProperty("client.impl"))
        ? new UsersApiClient()
        : new UsersDbClient();
  }
}
