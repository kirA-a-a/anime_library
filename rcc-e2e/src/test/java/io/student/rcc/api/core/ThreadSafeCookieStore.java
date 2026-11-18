package io.student.rcc.api.core;

import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

/**
 * Thread-safe обёртка над {@link CookieStore}.
 *
 * <p><b>Декоратор</b> — объект, который реализует тот же интерфейс, что и «внутренний»
 * объект, принимает его в себя и прокидывает вызовы дальше. Снаружи кажется обычный
 * {@code CookieStore}, а внутри можно добавить поведение (здесь — отдельное хранилище на каждый
 * поток через {@link ThreadLocal}).
 *
 * <p>Зачем так: OkHttp/CookieManager ждут один {@code CookieStore}, а тесты бегут
 * параллельно. Без декоратора куки разных потоков перемешаются.
 */
public enum ThreadSafeCookieStore implements CookieStore {
  INSTANCE;
  private final ThreadLocal<CookieStore> threadLocalCookieStore = ThreadLocal.withInitial(
      this::inMemoryCookieStore
  );

  private CookieStore inMemoryCookieStore() {
    return new CookieManager().getCookieStore();
  }

  @Override
  public void add(URI uri, HttpCookie cookie) {
    getStore().add(uri, cookie);
  }

  @Override
  public List<HttpCookie> get(URI uri) {
    return getStore().get(uri);
  }

  @Override
  public List<HttpCookie> getCookies() {
    return getStore().getCookies();
  }

  @Override
  public List<URI> getURIs() {
    return getStore().getURIs();
  }

  @Override
  public boolean remove(URI uri, HttpCookie cookie) {
    return getStore().remove(uri, cookie);
  }

  @Override
  public boolean removeAll() {
    return getStore().removeAll();
  }

  public String cookieValue(String name) {
    return getCookies().stream()
        .filter(c -> name.equals(c.getName()))
        .map(HttpCookie::getValue)
        .findFirst()
        .orElseThrow();
  }

  private CookieStore getStore() {
    return threadLocalCookieStore.get();
  }
}
