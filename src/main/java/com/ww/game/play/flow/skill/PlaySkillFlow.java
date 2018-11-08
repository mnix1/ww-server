package com.ww.game.play.flow.skill;

import com.ww.game.GameFlow;
import com.ww.game.member.MemberWisieManager;
import com.ww.game.play.PlayManager;
import com.ww.game.play.state.skill.hint.PlaySkillHintReceivedState;

public abstract class PlaySkillFlow extends GameFlow {
    protected MemberWisieManager manager;
    public PlaySkillFlow(MemberWisieManager manager) {
        this.manager = manager;
    }

    public abstract void start();
}
