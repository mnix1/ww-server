package com.ww.model.constant.social;

import lombok.Getter;

@Getter
public enum ExperienceSource {
    PRACTISE_LOST(1),
    PRACTISE_WIN(2),
    ;

    private int gain;

    private ExperienceSource(int gain) {
        this.gain = gain;
    }
}
