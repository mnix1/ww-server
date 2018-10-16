package com.ww.model.container.rival.challenge;

import com.ww.model.container.rival.RivalModel;
import com.ww.model.container.rival.war.WarInterval;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ChallengeInterval extends WarInterval {
    private RivalModel container;

    public long getAnsweringInterval() {
        int taskIndex = Math.max(0, container.getCurrentTaskIndex());
        long interval = super.getAnsweringInterval() - taskIndex * calculateInterval(5);
        return Math.max(interval, calculateInterval(15));
    }
}
