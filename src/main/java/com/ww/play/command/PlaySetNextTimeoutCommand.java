package com.ww.play.command;

import com.ww.play.container.PlayContainer;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class PlaySetNextTimeoutCommand extends PlayCommand {
    private long interval;

    public PlaySetNextTimeoutCommand(PlayContainer container, long interval) {
        super(container);
        this.interval = interval;
    }

    @Override
    public void execute() {
        container.getTimeouts().setNextTimeout(Instant.now().plus(interval, ChronoUnit.MILLIS));
    }
}
