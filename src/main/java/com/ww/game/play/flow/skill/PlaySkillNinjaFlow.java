package com.ww.game.play.flow.skill;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.play.state.skill.ghost.PlaySkillScareSucceededState;
import com.ww.game.play.state.skill.ninja.*;

public class PlaySkillNinjaFlow extends PlaySkillFlow {
    protected MemberWisieManager opponentManager;

    public PlaySkillNinjaFlow(MemberWisieManager manager, MemberWisieManager opponentManager) {
        super(manager);
        this.opponentManager = opponentManager;
        initStateMap();
    }

    protected void initStateMap() {
        stateMap.put("PREPARING_NINJA", () -> new PlaySkillPreparingNinjaState(this, manager));
        stateMap.put("KIDNAPPING",() ->  new PlaySkillKidnappingState(this, manager, opponentManager));
        stateMap.put("KIDNAP_SUCCEEDED",() ->  new PlaySkillKidnapSucceededState(this, manager, opponentManager));
        stateMap.put("KIDNAP_FAILED",() ->  new PlaySkillKidnapFailedState(this, manager, opponentManager));
        stateMap.put("WAS_NOT_KIDNAPPED",() ->  new PlaySkillWasNotKidnappedState(this, opponentManager));
        stateMap.put("REMOVING_NINJA",() ->  new PlaySkillRemovingNinjaState(this, manager));
        stateMap.put("REMOVED_NINJA", () -> new PlaySkillRemovedNinjaState(this, manager));
    }

    public void start() {
        run("PREPARING_NINJA");
    }
}
