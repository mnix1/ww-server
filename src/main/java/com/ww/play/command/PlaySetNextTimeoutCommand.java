package com.ww.play.command;

import com.ww.play.container.PlayContainer;

public class PlaySetNextTimeoutCommand extends PlayCommand {
    private long interval;

    public PlaySetNextTimeoutCommand(PlayContainer container, long interval) {
        super(container);
        this.interval = interval;
    }

    @Override
    public void execute() {
        container.getTimeouts().fromInterval(interval);
    }
}
