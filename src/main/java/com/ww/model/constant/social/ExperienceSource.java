package com.ww.model.constant.social;

import lombok.Getter;

@Getter
public enum ExperienceSource {
    PRACTISE_LOST(1),
    PRACTISE_WIN(4),
    BATTLE_LOST(2),
    BATTLE_WIN(10),
    WAR_LOST(4),
    WAR_WIN(20),
    CAMPAIGN_LOST(10),
    CAMPAIGN_WIN(50),
    ;

    private int gain;

    private ExperienceSource(int gain) {
        this.gain = gain;
    }
}
