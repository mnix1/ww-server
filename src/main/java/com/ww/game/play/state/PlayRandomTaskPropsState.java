package com.ww.game.play.state;

import com.ww.game.play.PlayManager;
import com.ww.game.play.command.PlayPrepareNextTaskCommand;
import com.ww.model.constant.Category;
import com.ww.model.constant.Language;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import lombok.Getter;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayModelFiller.fillModelTaskMeta;

@Getter
public class PlayRandomTaskPropsState extends PlayState {
    private Category category;
    private DifficultyLevel difficultyLevel;
    private Language language;

    public PlayRandomTaskPropsState(PlayManager manager) {
        super(manager, RivalStatus.RANDOM_TASK_PROPS);
    }

    @Override
    public void initProps() {
        category = Category.random();
        difficultyLevel = DifficultyLevel.random();
        language = getContainer().getInit().getCommonLanguage();
    }

    @Override
    public Map<String, Object> exportProps() {
        Map<String, Object> props = super.exportProps();
        props.put("category", category);
        props.put("difficultyLevel", difficultyLevel);
        props.put("language", language);
        return props;
    }

    @Override
    public void loadProps(Map<String, Object> props) {
        this.category = (Category) props.get("category");
        this.difficultyLevel = (DifficultyLevel) props.get("difficultyLevel");
        this.language = (Language) props.get("language");
    }

    @Override
    public void initCommands() {
        commands.add(new PlayPrepareNextTaskCommand(getContainer(), category, difficultyLevel, language));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelTaskMeta(model, getContainer());
        return model;
    }

    @Override
    public long afterInterval() {
        return manager.getInterval().getRandomTaskPropsInterval();
    }

    @Override
    public void after() {
        manager.getFlow().run("PREPARING_NEXT_TASK");
    }

}
