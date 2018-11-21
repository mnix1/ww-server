package com.ww.game.auto.flow;

import com.ww.game.GameFlow;
import com.ww.game.auto.AutoManager;
import com.ww.game.auto.state.AutoManageBooksState;
import com.ww.game.auto.state.AutoStartRivalState;
import com.ww.game.auto.state.AutoUpgradeWisiesState;
import com.ww.game.auto.state.rival.*;
import com.ww.game.auto.state.wisor.AutoWisorAnswerState;
import com.ww.game.auto.state.wisor.AutoWisorThinkingState;
import com.ww.game.play.state.skill.ghost.PlaySkillPreparingGhostState;

public class AutoWisorFlow extends GameFlow {
    private AutoManager manager;

    public AutoWisorFlow(AutoManager manager){
        this.manager = manager;
        initStateMap();
    }

    @Override
    protected void initStateMap() {
        stateMap.put("THINKING", () -> new AutoWisorThinkingState(this, manager));
        stateMap.put("ANSWER", () -> new AutoWisorAnswerState(this, manager));
    }

    @Override
    public void start() {
        run("THINKING");
    }
}
