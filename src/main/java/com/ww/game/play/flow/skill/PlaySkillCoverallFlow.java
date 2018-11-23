package com.ww.game.play.flow.skill;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.play.state.skill.coverall.PlaySkillCoverallState;
import com.ww.game.play.state.skill.coverall.PlaySkillPreparingCoverallState;

public class PlaySkillCoverallFlow extends PlaySkillFlow {
    protected MemberWisieManager opponentManager;

    public PlaySkillCoverallFlow(MemberWisieManager manager, MemberWisieManager opponentManager) {
        super(manager);
        this.opponentManager = opponentManager;
        initStateMap();
    }

    @Override
    protected void initStateMap() {
        stateMap.put("PREPARING_COVERALL", () -> new PlaySkillPreparingCoverallState(this, manager));
        stateMap.put("COVERALL", () -> new PlaySkillCoverallState(this, manager, opponentManager));
    }

    @Override
    public void start() {
        super.start();
        run("PREPARING_COVERALL");
    }
}
