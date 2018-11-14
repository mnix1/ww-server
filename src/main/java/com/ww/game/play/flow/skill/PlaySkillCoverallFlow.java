package com.ww.game.play.flow.skill;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.play.state.skill.coverall.PlaySkillCoverallState;
import com.ww.game.play.state.skill.coverall.PlaySkillPreparingCoverallState;
import com.ww.game.play.state.skill.pizza.*;

public class PlaySkillCoverallFlow extends PlaySkillFlow {
    protected MemberWisieManager opponentManager;

    public PlaySkillCoverallFlow(MemberWisieManager manager, MemberWisieManager opponentManager) {
        super(manager);
        this.opponentManager = opponentManager;
        initStateMap();
    }

    protected void initStateMap() {
        stateMap.put("PREPARING_COVERALL", new PlaySkillPreparingCoverallState(this, manager));
        stateMap.put("COVERALL", new PlaySkillCoverallState(this, manager, opponentManager));
    }

    public void start() {
        run("PREPARING_COVERALL");
    }
}
