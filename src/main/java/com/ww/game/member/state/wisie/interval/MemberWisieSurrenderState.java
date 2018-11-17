package com.ww.game.member.state.wisie.interval;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.state.wisie.MemberWisieState;
import com.ww.model.constant.wisie.MemberWisieStatus;

public class MemberWisieSurrenderState extends MemberWisieState {
    public MemberWisieSurrenderState(MemberWisieManager manager) {
        super(manager, MemberWisieStatus.SURRENDER);
    }
}
