package com.ww.play.war.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.play.PlayState;

public class PlayChoosingWhoAnswerState extends PlayState {
    public PlayChoosingWhoAnswerState() {
        this.status = RivalStatus.CHOOSING_WHO_ANSWER;
    }
}
