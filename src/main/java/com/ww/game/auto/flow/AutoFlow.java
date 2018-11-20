package com.ww.game.auto.flow;

import com.ww.game.GameFlow;
import com.ww.game.auto.AutoManager;
import com.ww.game.auto.state.AutoManageBooksState;
import com.ww.game.auto.state.AutoStartRivalState;
import com.ww.game.auto.state.AutoUpgradeWisiesState;
import com.ww.game.auto.state.rival.AutoRivalChoosingTaskCategoryState;
import com.ww.game.auto.state.rival.AutoRivalChoosingTaskDifficultyState;
import com.ww.game.auto.state.rival.AutoRivalChoosingWhoAnswerState;
import com.ww.game.auto.state.rival.AutoRivalIntroState;

public class AutoFlow extends GameFlow {
    private AutoManager manager;

    public AutoFlow(AutoManager manager){
        this.manager = manager;
        initStateMap();
    }

    @Override
    protected void initStateMap() {
        stateMap.put("MANAGE_BOOKS", () -> new AutoManageBooksState(manager));
        stateMap.put("UPGRADE_WISIES", () -> new AutoUpgradeWisiesState(manager));
        stateMap.put("START_RIVAL", () -> new AutoStartRivalState(manager));
        stateMap.put("RIVAL_INTRO", () -> new AutoRivalIntroState(manager));
        stateMap.put("RIVAL_CHOOSING_WHO_ANSWER", () -> new AutoRivalChoosingWhoAnswerState(manager));
        stateMap.put("RIVAL_CHOOSING_TASK_CATEGORY", () -> new AutoRivalChoosingTaskCategoryState(manager));
        stateMap.put("RIVAL_CHOOSING_TASK_DIFFICULTY", () -> new AutoRivalChoosingTaskDifficultyState(manager));
    }
}
