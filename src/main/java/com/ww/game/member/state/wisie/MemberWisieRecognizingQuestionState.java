package com.ww.game.member.state.wisie;

import com.ww.game.member.container.MemberWisieContainer;
import com.ww.model.constant.wisie.MemberWisieStatus;

public class MemberWisieRecognizingQuestionState extends MemberWisieState {
    public MemberWisieRecognizingQuestionState(MemberWisieContainer container) {
        super(container, MemberWisieStatus.RECOGNIZING_QUESTION);
    }
}
