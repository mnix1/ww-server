package com.ww.game.play.state;

import com.ww.game.play.PlayManager;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.game.play.command.PlaySetTaskCategoryCommand;
import com.ww.game.play.command.PlaySetTaskDifficultyCommand;
import com.ww.game.play.container.PlayContainer;

import java.util.Map;

public class PlayChosenTaskCategoryState extends PlayState {
    private Category category;

    public PlayChosenTaskCategoryState(PlayManager manager, Category category) {
        super(manager, RivalStatus.CHOSEN_TASK_CATEGORY);
        this.category = category;
    }

    @Override
    public void initCommands() {
        commands.add(new PlaySetTaskCategoryCommand(getContainer(), category));
    }

    @Override
    public void updateNotify(){
    }

    @Override
    public void after() {
        manager.getFlow().run("CHOOSING_TASK_DIFFICULTY");
    }

}
