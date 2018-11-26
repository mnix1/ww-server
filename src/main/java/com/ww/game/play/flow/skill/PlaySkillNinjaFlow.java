package com.ww.game.play.flow.skill;

import com.ww.game.play.container.skill.PlayWarAnsweringFlowContainer;
import com.ww.game.play.state.skill.ninja.*;

public class PlaySkillNinjaFlow extends PlaySkillFlowOpponent {

    public PlaySkillNinjaFlow(PlayWarAnsweringFlowContainer flowContainer, Long creatorProfileId, Long opponentProfileId) {
        super(flowContainer, creatorProfileId, opponentProfileId);
    }

    @Override
    protected void initStateMap() {
        stateMap.put("PREPARING_NINJA", () -> new PlaySkillPreparingNinjaState(this));
        stateMap.put("KIDNAPPING",() ->  new PlaySkillKidnappingState(this));
        stateMap.put("KIDNAP_SUCCEEDED",() ->  new PlaySkillKidnapSucceededState(this));
        stateMap.put("REMOVING_NINJA",() ->  new PlaySkillRemovingNinjaState(this));
        stateMap.put("REMOVED_NINJA", () -> new PlaySkillRemovedNinjaState(this));
    }

    @Override
    public void start() {
        super.start();
        run("PREPARING_NINJA");
    }
}
