package com.ww.game.play.state.war;

import com.ww.game.play.PlayManager;
import com.ww.game.play.command.skill.PlaySkillUnblockAllCommand;
import com.ww.game.play.state.PlayChoosingTaskCategoryState;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelSkills;

public class PlayWarChoosingTaskCategoryState extends PlayChoosingTaskCategoryState {
    public PlayWarChoosingTaskCategoryState(PlayManager manager) {
        super(manager);
    }

    @Override
    public void initCommands() {
        super.initCommands();
        for (RivalTeam team : getContainer().getTeams().getTeams()) {
            commands.add(new PlaySkillUnblockAllCommand((WarTeam) team));
        }
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelSkills(model, (WarTeam) team, (WarTeam) opponentTeam);
        return model;
    }
}
