package com.ww.model.container.rival.challenge;

import com.ww.model.container.rival.war.WarInterval;
import com.ww.game.play.container.PlayContainer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ChallengeInterval extends WarInterval {
    private PlayContainer container;

    @Override
    public long getAnsweringInterval() {
        int taskIndex = Math.max(0, container.getTasks().taskIndex());
        long interval = super.getAnsweringInterval() - taskIndex * calculateInterval(2);
        return Math.max(interval, calculateInterval(15));
    }
}
