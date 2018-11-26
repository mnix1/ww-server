package com.ww.game.play.state.skill;

import com.ww.game.GameState;
import com.ww.game.play.PlayManager;
import com.ww.game.play.container.skill.PlayWarAnsweringFlowContainer;
import com.ww.game.play.state.war.PlayWarAnsweringState;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelSkills;

public class PlaySkillActionState extends GameState {
    protected PlayManager manager;
    protected PlayWarAnsweringFlowContainer flowContainer;

    public PlaySkillActionState(PlayManager manager) {
        this.manager = manager;
        this.flowContainer = ((PlayWarAnsweringState) manager.getFlow().currentState()).getFlowContainer();
    }

}
