package com.ww.game.play.flow.skill;

import com.ww.game.GameFlow;
import com.ww.game.play.container.skill.PlayWarAnsweringFlowContainer;
import com.ww.game.play.state.skill.changetask.PlaySkillSubmitApplicationState;
import com.ww.game.play.state.skill.changetask.PlaySkillWantChangeTaskState;

public class PlaySkillChangeTaskFlow extends PlaySkillFlow {

    public PlaySkillChangeTaskFlow(PlayWarAnsweringFlowContainer flowContainer, Long creatorProfileId) {
        super(flowContainer, creatorProfileId);
    }

    @Override
    protected void initStateMap() {
        stateMap.put("WANT_CHANGE_TASK", () -> new PlaySkillWantChangeTaskState(this));
        stateMap.put("SUBMIT_APPLICATION", () -> new PlaySkillSubmitApplicationState(this));
    }

    @Override
    public void start() {
        super.start();
        run("WANT_CHANGE_TASK");
    }
}
