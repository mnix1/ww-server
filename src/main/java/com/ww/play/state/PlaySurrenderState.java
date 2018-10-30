package com.ww.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.play.container.PlayContainer;
import lombok.Getter;

@Getter
public class PlaySurrenderState extends PlayState {
    private Long profileId;

    public PlaySurrenderState(PlayContainer container, Long profileId) {
        super(container, RivalStatus.CLOSED);
        this.profileId = profileId;
    }
}
