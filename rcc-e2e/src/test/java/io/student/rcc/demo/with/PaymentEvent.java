package io.student.rcc.demo.with;

import lombok.With;

/**
 * Демо: {@code @With} только на одном поле → только {@code withAmount(...)}.
 */
public record PaymentEvent(
    String paymentId,
    String currency,
    @With Double amount
) {
}
