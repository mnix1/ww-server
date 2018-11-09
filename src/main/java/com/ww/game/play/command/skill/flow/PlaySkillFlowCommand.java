package com.ww.game.play.command.skill.flow;

import com.ww.game.GameCommand;
import com.ww.game.play.PlayManager;
import com.ww.game.play.command.PlayCommand;
import com.ww.game.play.container.PlayContainer;

public abstract class PlaySkillFlowCommand extends GameCommand {
    protected PlayManager manager;

    protected PlaySkillFlowCommand(PlayManager manager) {
        this.manager = manager;
    }
}
