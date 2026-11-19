package io.student.rcc.demo.with;

import lombok.With;

/**
 * Демо: {@code @With} на всём record → wither на каждое поле.
 */
@With
public record OrderEvent(
    String orderId,
    String status,
    String customerId
) {
}
