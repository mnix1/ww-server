package com.ww.game.play.state.war;

import com.ww.game.play.PlayManager;
import com.ww.game.play.state.PlaySurrenderState;
import lombok.Getter;

@Getter
public class PlayWarSurrenderState extends PlaySurrenderState {

    public PlayWarSurrenderState(PlayManager manager, Long profileId) {
        super(manager, profileId);
    }
}
