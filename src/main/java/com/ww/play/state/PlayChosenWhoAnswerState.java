package com.ww.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.play.container.PlayContainer;

public class PlayChosenWhoAnswerState extends PlayState {
    public PlayChosenWhoAnswerState(PlayContainer container) {
        super(container, RivalStatus.CHOSEN_WHO_ANSWER);
    }
}
