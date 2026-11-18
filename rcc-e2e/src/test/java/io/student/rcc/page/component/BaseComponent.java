package io.student.rcc.page.component;

import com.codeborne.selenide.SelenideElement;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Базовый UI-компонент (кусок страницы: хедер, тост, карточка…).
 *
 * <p><b>Зачем вообще BaseComponent</b>: общие поля/методы для всех компонентов
 * (например {@link #self}). Написал один раз — пользуются все компоненты.
 *
 * <p><b>Зачем {@code T extends BaseComponent<T>}</b>: методы базы возвращают конкретный
 * компонент ({@code Header}, {@code Toast}…), а не {@code BaseComponent}. Иначе после
 * вызова метода из базы цепочка «отваливается» к общему типу и методы дочернего
 * компонента недоступны без cast.
 *
 * <p>Как объявлять компонент:
 * <pre>{@code
 * public class Header extends BaseComponent<Header> { ... }
 * }</pre>
 *
 * @param <T> тип конкретного компонента (обычно сам компонент)
 */
@ParametersAreNonnullByDefault
@SuppressWarnings("UnusedReturnValue")
public class BaseComponent<T extends BaseComponent<T>> {

  protected final SelenideElement self;

  public BaseComponent(SelenideElement self) {
    this.self = self;
  }
}
