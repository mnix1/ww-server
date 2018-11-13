package com.ww.game.play.command;

import com.ww.game.play.container.PlayContainer;
import com.ww.model.constant.Category;
import com.ww.model.constant.Language;
import com.ww.model.constant.rival.DifficultyLevel;

public class PlayPrepareRandomNextTaskCommand extends PlayCommand {
    public PlayPrepareRandomNextTaskCommand(PlayContainer container) {
        super(container);
    }

    @Override
    public void execute() {
        container.getTasks().prepareNext(Category.random(), DifficultyLevel.random(), container.getInit().getCommonLanguage());
        container.getDecisions().defaultAnswered();
    }
}
