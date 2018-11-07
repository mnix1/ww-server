package com.ww.game.play.flow;

import com.ww.game.GameFlow;
import com.ww.game.GameState;
import com.ww.game.play.PlayManager;
import com.ww.game.play.state.*;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.DifficultyLevel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayFlow extends GameFlow {
    protected PlayManager manager;

    public PlayFlow(PlayManager manager) {
        this.manager = manager;
        initStateMap();
    }

    protected void initStateMap() {
        stateMap.put("INTRO", createIntroState());
        stateMap.put("RANDOM_TASK_PROPS", createRandomTaskPropsState());
        stateMap.put("CHOOSING_TASK_CATEGORY", createChoosingTaskCategoryState());
        stateMap.put("CHOOSING_TASK_DIFFICULTY", createChoosingTaskDifficultyState());
        stateMap.put("CHOOSING_TASK_PROPS_TIMEOUT", createChoosingTaskPropsTimeoutState());
        stateMap.put("PREPARING_NEXT_TASK", createPreparingNextTaskState());
        stateMap.put("ANSWERING", createAnsweringState());
        stateMap.put("ANSWERING_TIMEOUT", createAnsweringTimeoutState());
        stateMap.put("END", createEndState());
    }

    @Override
    protected void addState(GameState state) {
        manager.getContainer().addState(state);
    }

    protected PlayIntroState createIntroState() {
        return new PlayIntroState(manager);
    }

    protected PlayRandomTaskPropsState createRandomTaskPropsState() {
        return new PlayRandomTaskPropsState(manager);
    }

    protected PlayChoosingTaskCategoryState createChoosingTaskCategoryState() {
        return new PlayChoosingTaskCategoryState(manager);
    }

    protected PlayChoosingTaskDifficultyState createChoosingTaskDifficultyState() {
        return new PlayChoosingTaskDifficultyState(manager);
    }

    protected PlayChoosingTaskPropsTimeoutState createChoosingTaskPropsTimeoutState() {
        return new PlayChoosingTaskPropsTimeoutState(manager);
    }

    protected PlayPreparingNextTaskState createPreparingNextTaskState() {
        return new PlayPreparingNextTaskState(manager);
    }

    protected PlayAnsweringState createAnsweringState() {
        return new PlayAnsweringState(manager);
    }

    protected PlayAnsweringTimeoutState createAnsweringTimeoutState() {
        return new PlayAnsweringTimeoutState(manager);
    }

    protected PlayEndState createEndState() {
        return new PlayEndState(manager);
    }

    public void surrenderAction(Long profileId) {
        run(createSurrenderState(profileId));
    }

    protected PlaySurrenderState createSurrenderState(Long profileId) {
        return new PlaySurrenderState(manager, profileId);
    }

    public void answeredAction(Long profileId, Long answerId) {
        run(createAnsweredState(profileId, answerId));
    }

    protected PlayState createAnsweredState(Long profileId, Long answerId) {
        return new PlayAnsweredState(manager, profileId, answerId);
    }

    public void chosenTaskCategoryAction(Category category) {
        run(createChosenTaskCategoryState(category));
    }

    protected PlayChosenTaskCategoryState createChosenTaskCategoryState(Category category) {
        return new PlayChosenTaskCategoryState(manager, category);
    }

    public void chosenTaskDifficultyAction(DifficultyLevel difficultyLevel) {
        run(createChosenTaskDifficultyState(difficultyLevel));
    }

    protected PlayChosenTaskDifficultyState createChosenTaskDifficultyState(DifficultyLevel difficultyLevel) {
        return new PlayChosenTaskDifficultyState(manager, difficultyLevel);
    }
}
