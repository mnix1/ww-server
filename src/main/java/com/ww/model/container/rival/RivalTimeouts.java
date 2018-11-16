package com.ww.model.container.rival;

import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@ToString
public class RivalTimeouts {
    @Getter
    protected Instant nextTimeout;
    @Getter
    protected Instant currentTimeout;

    public void fromInterval(long interval) {
        currentTimeout = Instant.now();
        nextTimeout = currentTimeout.plus(interval, ChronoUnit.MILLIS);
    }
}
