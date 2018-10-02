package com.ww.manager.rival.state;

import com.ww.manager.rival.RivalManager;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.constant.rival.DifficultyLevel;
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
        manager.getModel().setStatus(RivalStatus.CHOOSING_TASK_PROPS);
        manager.getModel().increaseCurrentTaskIndex();
        boolean randomChooseTaskProps = manager.getModel().randomChooseTaskProps();
        int interval;
        if (randomChooseTaskProps) {
            manager.prepareTask((long) manager.getModel().getCurrentTaskIndex() + 1);
            interval = manager.getInterval().getRandomChooseTaskPropsInterval();
        } else {
            manager.getModel().setChosenCategory(Category.RANDOM);
            manager.getModel().setIsChosenCategory(false);
            manager.getModel().setChosenDifficulty(DifficultyLevel.NORMAL);
            manager.getModel().setIsChosenDifficulty(false);
            interval = manager.getInterval().getChoosingTaskPropsInterval();
        }
        manager.getModel().setEndChoosingTaskPropsDate(Instant.now().plus(interval, ChronoUnit.MILLIS));
        manager.getModel().getTeams().forEachTeam(profileContainer -> {
            Map<String, Object> model = new HashMap<>();
            manager.getModelFactory().fillModelChoosingTaskProps(model, profileContainer);
            manager.send(model, manager.getMessageContent(), profileContainer.getProfileId());
        });
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
