package io.student.rcc.demo.with;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.Test;

class WithAnnotationDemoTest {

  @Test
  void shouldCopyOrderEventChangingOneField() {
    OrderEvent original = new OrderEvent("ord-1", "NEW", "cust-42");

    OrderEvent updated = original.withStatus("PROCESSED");

    assertNotSame(original, updated);
    assertEquals("ord-1", updated.orderId());
    assertEquals("PROCESSED", updated.status());
    assertEquals("cust-42", updated.customerId());
    assertEquals("NEW", original.status());
  }
}
