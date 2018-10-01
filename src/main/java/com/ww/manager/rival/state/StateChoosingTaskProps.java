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
        manager.getContainer().setStatus(RivalStatus.CHOOSING_TASK_PROPS);
        manager.getContainer().increaseCurrentTaskIndex();
        boolean randomChooseTaskProps = manager.getContainer().randomChooseTaskProps();
        int interval;
        if (randomChooseTaskProps) {
            manager.prepareTask((long) manager.getContainer().getCurrentTaskIndex() + 1);
            interval = manager.getInterval().getRandomChooseTaskPropsInterval();
        } else {
            manager.getContainer().setChosenCategory(Category.RANDOM);
            manager.getContainer().setIsChosenCategory(false);
            manager.getContainer().setChosenDifficulty(DifficultyLevel.NORMAL);
            manager.getContainer().setIsChosenDifficulty(false);
            interval = manager.getInterval().getChoosingTaskPropsInterval();
        }
        manager.getContainer().setEndChoosingTaskPropsDate(Instant.now().plus(interval, ChronoUnit.MILLIS));
        manager.getContainer().forEachProfile(rivalProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            manager.getModelFactory().fillModelChoosingTaskProps(model, rivalProfileContainer);
            manager.send(model, manager.getMessageContent(), rivalProfileContainer.getProfileId());
        });
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
