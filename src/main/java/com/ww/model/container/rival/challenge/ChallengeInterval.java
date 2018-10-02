package com.ww.model.container.rival.challenge;

import com.ww.model.container.rival.RivalModel;
import com.ww.model.container.rival.war.WarInterval;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ChallengeInterval extends WarInterval {
    private RivalModel container;

    public Integer getAnsweringInterval() {
        int taskIndex = Math.max(0, container.getCurrentTaskIndex());
        Integer interval = super.getAnsweringInterval() - taskIndex * 5000;
        return Math.max(interval, 5000);
    }
}
