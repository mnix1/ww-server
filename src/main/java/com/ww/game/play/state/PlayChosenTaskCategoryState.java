package com.ww.game.play.state;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.game.play.command.PlaySetTaskCategoryCommand;
import com.ww.game.play.command.PlaySetTaskDifficultyCommand;
import com.ww.game.play.container.PlayContainer;

import java.util.Map;

public class PlayChosenTaskCategoryState extends PlayState {
    private Category category;

    public PlayChosenTaskCategoryState(PlayContainer container, Category category) {
        super(container, RivalStatus.CHOSEN_TASK_CATEGORY);
        this.category = category;
    }

    @Override
    public void initCommands() {
        commands.add(new PlaySetTaskCategoryCommand(container, category));
    }

}
