package com.ww.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.play.PlayState;
import lombok.Getter;

@Getter
public class PlaySurrenderState extends PlayState {
    private Long profileId;

    public PlaySurrenderState(Long profileId) {
        this.status = RivalStatus.CLOSED;
        this.profileId = profileId;
    }
}
