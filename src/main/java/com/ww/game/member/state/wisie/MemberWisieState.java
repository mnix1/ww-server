package com.ww.game.member.state.wisie;

import com.ww.game.GameState;
import com.ww.game.member.container.MemberWisieContainer;
import com.ww.model.constant.wisie.MemberWisieStatus;
import com.ww.model.container.rival.war.WarWisie;
import lombok.Getter;

public class MemberWisieState extends GameState {
    protected MemberWisieContainer container;
    protected MemberWisieStatus status;
    @Getter
    protected long interval;

    protected WarWisie getWisie(){
        return container.getMember().getContent();
    }

    public MemberWisieState(MemberWisieContainer container, MemberWisieStatus status) {
        this.container = container;
        this.status = status;
        this.interval = prepareInterval();
    }

    protected long prepareInterval() {
        return 0;
    }
}
