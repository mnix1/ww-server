package com.ww.game.play.flow.skill;

import com.ww.game.play.container.skill.PlayWarAnsweringFlowContainer;
import com.ww.game.play.state.skill.coverall.PlaySkillCoverallState;
import com.ww.game.play.state.skill.coverall.PlaySkillDoneCoverallState;
import com.ww.game.play.state.skill.coverall.PlaySkillPreparingCoverallState;

public class PlaySkillCoverallFlow extends PlaySkillFlowOpponent {
    public PlaySkillCoverallFlow(PlayWarAnsweringFlowContainer flowContainer, Long creatorProfileId, Long opponentProfileId) {
        super(flowContainer, creatorProfileId, opponentProfileId);
    }

    @Override
    protected void initStateMap() {
        stateMap.put("PREPARING_COVERALL", () -> new PlaySkillPreparingCoverallState(this));
        stateMap.put("COVERALL", () -> new PlaySkillCoverallState(this));
        stateMap.put("DONE_COVERALL", () -> new PlaySkillDoneCoverallState(this));
    }

    @Override
    public void start() {
        super.start();
        run("PREPARING_COVERALL");
    }
}
