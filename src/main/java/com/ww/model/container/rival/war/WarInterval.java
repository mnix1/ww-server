package com.ww.model.container.rival.war;

import com.ww.model.container.rival.RivalInterval;

public class WarInterval extends RivalInterval {
    @Override
    public long getIntroInterval() {
        return calculateInterval(5);
    }

    public long getChoosingWhoAnswerInterval() {
        return calculateInterval(10);
    }
}
