package com.ww.game.play.flow.skill;

import com.ww.game.play.container.skill.PlayWarAnsweringFlowContainer;
import com.ww.game.play.state.skill.ghost.*;

public class PlaySkillGhostFlow extends PlaySkillFlowOpponent {

    public PlaySkillGhostFlow(PlayWarAnsweringFlowContainer flowContainer, Long creatorProfileId, Long opponentProfileId) {
        super(flowContainer, creatorProfileId, opponentProfileId);
    }

    @Override
    protected void initStateMap() {
        stateMap.put("PREPARING_GHOST", () -> new PlaySkillPreparingGhostState(this));
        stateMap.put("SCARING", () -> new PlaySkillScaringState(this));
        stateMap.put("SCARE_SUCCEEDED", () -> new PlaySkillScareSucceededState(this));
        stateMap.put("SCARE_FAILED", () -> new PlaySkillScareFailedState(this));
        stateMap.put("WAS_NOT_SCARED", () -> new PlaySkillWasNotScaredState(this));
        stateMap.put("REMOVING_GHOST", () -> new PlaySkillRemovingGhostState(this));
        stateMap.put("DISQUALIFICATION", () -> new PlaySkillDisqualificationState(this));
        stateMap.put("NO_DISQUALIFICATION", () -> new PlaySkillNoDisqualificationState(this));
        stateMap.put("DONE_GHOST", () -> new PlaySkillDoneGhostState(this));
    }

    @Override
    public void start() {
        super.start();
        run("PREPARING_GHOST");
    }
}
