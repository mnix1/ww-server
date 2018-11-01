package com.ww.game.member.state.wisie;

import com.ww.game.member.container.MemberContainer;
import com.ww.game.member.container.MemberWisieContainer;
import com.ww.model.constant.wisie.MemberWisieStatus;

public class MemberWisieWaitingForQuestionState extends MemberWisieState {
    public MemberWisieWaitingForQuestionState(MemberWisieContainer container) {
        super(container, MemberWisieStatus.WAITING_FOR_QUESTION);
    }

    @Override
    protected long prepareInterval() {
//        getWisie().
        return 0;
    }
}
