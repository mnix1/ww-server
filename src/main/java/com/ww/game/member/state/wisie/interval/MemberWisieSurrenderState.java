package com.ww.game.member.state.wisie.interval;

import com.ww.game.member.MemberWisieManager;
import com.ww.model.constant.wisie.MemberWisieStatus;

import java.util.HashMap;
import java.util.Map;

public class MemberWisieSurrenderState extends MemberWisieIntervalState {
    public MemberWisieSurrenderState(MemberWisieManager manager) {
        super(manager, MemberWisieStatus.SURRENDER);
    }


}
