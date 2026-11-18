package io.student.rcc.condition;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Color {
  PURPLE("rgba(116, 74, 161, 1)"),
  YELLOW("rgba(230, 200, 51, 1)");

  private final String rgba;
}
