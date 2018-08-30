package com.ww.manager.rival.state;

import com.ww.manager.rival.RivalManager;
import com.ww.manager.rival.state.State;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import io.reactivex.Flowable;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class StateChoosingTaskProps extends State {

    public StateChoosingTaskProps(RivalManager manager) {
        super(manager);
    }

    @Override
    protected Flowable<Long> processFlowable() {
        rivalContainer.setStatus(RivalStatus.CHOOSING_TASK_PROPS);
        rivalContainer.increaseCurrentTaskIndex();
        boolean randomChooseTaskProps = rivalContainer.randomChooseTaskProps();
        int interval;
        if (randomChooseTaskProps) {
            rivalManager.prepareTask((long) rivalContainer.getCurrentTaskIndex() + 1);
            interval = rivalManager.getRandomChooseTaskPropsInterval();
        } else {
            rivalContainer.setChosenCategory(Category.RANDOM);
            rivalContainer.setIsChosenCategory(false);
            rivalContainer.setChosenDifficulty(TaskDifficultyLevel.EXTREMELY_EASY);
            rivalContainer.setIsChosenDifficulty(false);
            interval = rivalManager.getChoosingTaskPropsInterval();
        }
        rivalContainer.setEndChoosingTaskPropsDate(Instant.now().plus(interval, ChronoUnit.MILLIS));
        rivalContainer.forEachProfile(rivalProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            rivalContainer.fillModelChoosingTaskProps(model, rivalProfileContainer);
            rivalManager.send(model, rivalManager.getMessageContent(), rivalProfileContainer.getProfileId());
        });
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
