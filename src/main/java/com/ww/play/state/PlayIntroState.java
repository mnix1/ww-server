package com.ww.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.play.PlayState;

public class PlayIntroState extends PlayState {
    public PlayIntroState() {
        this.status = RivalStatus.INTRO;
    }
}
