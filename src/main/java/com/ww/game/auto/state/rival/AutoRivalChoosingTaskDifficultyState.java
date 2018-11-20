package com.ww.game.auto.state.rival;

import com.ww.game.auto.AutoManager;
import com.ww.helper.RandomHelper;
import com.ww.model.constant.rival.DifficultyLevel;

import static com.ww.service.rival.global.RivalMessageService.CHOOSE_TASK_DIFFICULTY;

public class AutoRivalChoosingTaskDifficultyState extends AutoRivalState {

    public AutoRivalChoosingTaskDifficultyState(AutoManager manager) {
        super(manager);
    }

    @Override
    public void execute() {
        super.execute();
        if (!container.isMeChoosingTaskProps()) {
            return;
        }
        long interval = RandomHelper.randomLong(1, (long) (container.interval().getChoosingTaskDifficultyInterval() * 0.75));
        sendAfter(interval, CHOOSE_TASK_DIFFICULTY, newModel("difficultyLevel", DifficultyLevel.random()));
    }
}
