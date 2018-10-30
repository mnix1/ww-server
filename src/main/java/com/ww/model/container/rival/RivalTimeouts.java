package com.ww.model.container.rival;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class RivalTimeouts {
    protected Instant nextTimeout;

    public Long nextInterval() {
        return toInterval(nextTimeout);
    }

    public void fromInterval(long interval){
        nextTimeout = Instant.now().plus(interval, ChronoUnit.MILLIS);
    }

    private Long toInterval(Instant instant) {
        return Math.max(instant.toEpochMilli() - Instant.now().toEpochMilli(), 0L);
    }
}
