package com.qwertgold.spring.aws.messaging;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class TestPayloadDto {
    String someString;
    Instant someInstant;
    BigDecimal someBigDecimal;
}
