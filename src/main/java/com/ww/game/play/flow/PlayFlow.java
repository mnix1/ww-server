package com.ww.game.play.flow;

import com.ww.game.GameFlow;
import com.ww.game.GameState;
import com.ww.game.play.PlayManager;
import com.ww.game.play.state.*;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.RivalStatus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayFlow extends GameFlow {
    protected PlayManager manager;

    public PlayFlow(PlayManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    protected void initStateMap() {
        stateMap.put("INTRO", this::createIntroState);
        stateMap.put("RANDOM_TASK_PROPS", this::createRandomTaskPropsState);
        stateMap.put("CHOOSING_TASK_CATEGORY", this::createChoosingTaskCategoryState);
        stateMap.put("CHOOSING_TASK_DIFFICULTY", this::createChoosingTaskDifficultyState);
        stateMap.put("CHOOSING_TASK_PROPS_TIMEOUT", this::createChoosingTaskPropsTimeoutState);
        stateMap.put("PREPARING_NEXT_TASK", this::createPreparingNextTaskState);
        stateMap.put("ANSWERING", this::createAnsweringState);
        stateMap.put("ANSWERING_TIMEOUT", this::createAnsweringTimeoutState);
        stateMap.put("END", this::createEndState);
    }

    @Override
    public void start(){
        super.start();
        run("INTRO");
    }

    public boolean isStatusEquals(RivalStatus status) {
        return ((PlayState) currentState().get()).getStatus() == status;
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
