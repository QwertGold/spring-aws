package com.hellopublic.spring.aws.messaging;

import com.hellopublic.spring.aws.messaging.core.domain.Destination;
import com.hellopublic.spring.aws.messaging.core.domain.Header;
import com.hellopublic.spring.aws.messaging.core.spi.Message;
import com.hellopublic.spring.aws.messaging.core.util.IdGenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

public class Helper {

    public static final String DESTINATION = "destination";

    public static Destination mockDestination() {
        return new Destination(DESTINATION, "MOCK");
    }

    public static TestPayloadDto createPayload() {
        return new TestPayloadDto()
                .setSomeString("A String with emojis \uD83D\uDE02\uD83D\uDE0D\uD83C\uDF89\uD83D\uDC4D")
                .setSomeBigDecimal(new BigDecimal("0.2"))
                .setSomeInstant(Instant.now())
                ;
    }

    public static Message createMessage() {
        return new Message()
                .setPayload(createPayload())
                .setHeaders(Map.of("name", new Header("String", "value")))
                .setClientId(IdGenerator.noClientId())
                ;
    }
}
