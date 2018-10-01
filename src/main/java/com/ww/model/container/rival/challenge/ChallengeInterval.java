package com.ww.model.container.rival.challenge;

import com.ww.model.container.rival.RivalContainer;
import com.ww.model.container.rival.RivalInterval;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ChallengeInterval extends RivalInterval {
    private RivalContainer container;

    public Integer getAnsweringInterval() {
        int taskIndex = Math.max(0, container.getCurrentTaskIndex());
        Integer interval = super.getAnsweringInterval() - taskIndex * 5000;
        return Math.max(interval, 5000);
    }
}
