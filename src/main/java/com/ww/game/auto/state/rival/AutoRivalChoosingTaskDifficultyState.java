package com.ww.game.auto.state.rival;

import com.ww.game.auto.AutoManager;
import com.ww.helper.RandomHelper;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.container.MapModel;

import static com.ww.helper.RandomHelper.randomDouble;
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
        long interval =(long) (container.interval().getChoosingTaskDifficultyInterval() * randomDouble(.1, 0.35));
        sendAfter(interval, CHOOSE_TASK_DIFFICULTY, new MapModel("difficultyLevel", DifficultyLevel.random()).get());
    }
}
