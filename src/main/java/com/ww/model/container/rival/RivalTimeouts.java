package com.ww.model.container.rival;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class RivalTimeouts {
    protected Instant nextTimeout;

    public Long nextInterval() {
        return toInterval(nextTimeout);
    }

    private Long toInterval(Instant instant) {
        return Math.max(instant.toEpochMilli() - Instant.now().toEpochMilli(), 0L);
    }
}
