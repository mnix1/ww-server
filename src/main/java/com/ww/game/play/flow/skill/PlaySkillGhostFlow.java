package com.ww.game.play.flow.skill;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.play.state.skill.ghost.*;

public class PlaySkillGhostFlow extends PlaySkillFlow {
    protected MemberWisieManager opponentManager;

    public PlaySkillGhostFlow(MemberWisieManager manager, MemberWisieManager opponentManager) {
        super(manager);
        this.opponentManager = opponentManager;
        initStateMap();
    }

    @Override
    protected void initStateMap() {
        stateMap.put("PREPARING_GHOST", () -> new PlaySkillPreparingGhostState(this, manager));
        stateMap.put("SCARING", () -> new PlaySkillScaringState(this, manager, opponentManager));
        stateMap.put("SCARE_SUCCEEDED", () -> new PlaySkillScareSucceededState(this, manager, opponentManager));
        stateMap.put("SCARE_FAILED", () -> new PlaySkillScareFailedState(this, manager, opponentManager));
        stateMap.put("WAS_NOT_SCARED", () -> new PlaySkillWasNotScaredState(this, opponentManager));
        stateMap.put("REMOVING_GHOST", () -> new PlaySkillRemovingGhostState(this, manager));
        stateMap.put("DISQUALIFICATION", () -> new PlaySkillDisqualificationState(this, manager));
        stateMap.put("NO_DISQUALIFICATION", () -> new PlaySkillNoDisqualificationState(this, manager));
    }

    @Override
    public void start() {
        super.start();
        run("PREPARING_GHOST");
    }
}
