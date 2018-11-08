package com.ww.game.play.flow;

import com.ww.game.GameState;
import com.ww.game.play.PlayManager;
import com.ww.game.play.state.*;
import com.ww.game.play.state.skill.hint.PlaySkillHintActionState;
import com.ww.game.play.state.skill.lifebuoy.PlaySkillLifebuoyActionState;
import com.ww.game.play.state.war.*;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.container.rival.war.WarTeam;

public class PlayWarFlow extends PlayFlow {

    public PlayWarFlow(PlayManager manager) {
        super(manager);
    }

    @Override
    protected void initStateMap() {
        super.initStateMap();
        stateMap.put("CHOOSING_WHO_ANSWER", createChoosingWhoAnswerState());
    }

    protected PlayState createChoosingWhoAnswerState() {
        return new PlayWarChoosingWhoAnswerState(manager);
    }

    @Override
    protected PlayIntroState createIntroState() {
        return new PlayWarIntroState(manager);
    }

    @Override
    protected PlayRandomTaskPropsState createRandomTaskPropsState() {
        return new PlayWarRandomTaskPropsState(manager);
    }

    @Override
    protected PlayChoosingTaskDifficultyState createChoosingTaskDifficultyState() {
        return new PlayChoosingTaskDifficultyState(manager);
    }

    @Override
    protected PlayChoosingTaskPropsTimeoutState createChoosingTaskPropsTimeoutState() {
        return new PlayWarChoosingTaskPropsTimeoutState(manager);
    }

    @Override
    protected PlayPreparingNextTaskState createPreparingNextTaskState() {
        return new PlayWarPreparingNextTaskState(manager);
    }

    @Override
    protected PlayAnsweringState createAnsweringState() {
        return new PlayWarAnsweringState(manager);
    }

    @Override
    protected PlayAnsweredState createAnsweredState(Long profileId, Long answerId) {
        return new PlayWarAnsweredState(manager, profileId, answerId);
    }

    @Override
    protected PlayAnsweringTimeoutState createAnsweringTimeoutState() {
        return new PlayWarAnsweringTimeoutState(manager);
    }

    public synchronized void chosenWhoAnswerAction(Long profileId, int activeIndex) {
        run(createChosenWhoAnswerState(profileId, activeIndex));
    }

    public PlayWarChosenWhoAnswerState createChosenWhoAnswerState(Long profileId, int activeIndex) {
        return new PlayWarChosenWhoAnswerState(manager, profileId, activeIndex);
    }

    @Override
    protected PlayChosenTaskDifficultyState createChosenTaskDifficultyState(DifficultyLevel difficultyLevel) {
        return new PlayWarChosenTaskDifficultyState(manager, difficultyLevel);
    }

    public synchronized void skillAction(GameState state){
        state.initCommands();
        state.execute();
        state.updateNotify();
    }
}
