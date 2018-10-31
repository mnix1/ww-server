package com.ww.game.play.command;

import com.ww.model.constant.Category;
import com.ww.game.play.container.PlayContainer;

public class PlaySetTaskCategoryCommand extends PlayCommand {
    private Category category;

    public PlaySetTaskCategoryCommand(PlayContainer container, Category category) {
        super(container);
        this.category = category;
    }

    @Override
    public void execute() {
        container.getDecisions().chosenCategory(category);
    }
}
