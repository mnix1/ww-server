package com.ww.game.play.command;

import com.ww.game.GameCommand;
import com.ww.model.constant.Category;
import com.ww.model.constant.Language;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.game.play.container.PlayContainer;

public class PlayPrepareNextTaskCommand extends GameCommand {
    private Category category;
    private DifficultyLevel difficultyLevel;
    private Language language;

    public PlayPrepareNextTaskCommand(PlayContainer container, Category category, DifficultyLevel difficultyLevel, Language language) {
        super(container);
        this.category = category;
        this.difficultyLevel = difficultyLevel;
        this.language = language;
    }

    @Override
    public void execute() {
        container.getTasks().prepareNext(category, difficultyLevel, language);
        container.getDecisions().defaultAnswered();
    }
}
