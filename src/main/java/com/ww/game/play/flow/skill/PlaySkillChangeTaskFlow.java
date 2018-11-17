package com.ww.game.play.flow.skill;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.play.state.skill.changetask.PlaySkillApplicationAcceptedState;
import com.ww.game.play.state.skill.changetask.PlaySkillSubmitApplicationState;
import com.ww.game.play.state.skill.changetask.PlaySkillWantChangeTaskState;
import com.ww.game.play.state.skill.hint.*;

public class PlaySkillChangeTaskFlow extends PlaySkillFlow {

    public PlaySkillChangeTaskFlow(MemberWisieManager manager) {
        super(manager);
        initStateMap();
    }

    protected void initStateMap() {
        stateMap.put("WANT_CHANGE_TASK", () -> new PlaySkillWantChangeTaskState(this, manager));
        stateMap.put("SUBMIT_APPLICATION", () -> new PlaySkillSubmitApplicationState(this, manager));
    }

    public void start() {
        run("WANT_CHANGE_TASK");
    }
}
