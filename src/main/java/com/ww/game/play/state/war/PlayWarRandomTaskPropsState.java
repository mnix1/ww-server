package com.ww.game.play.state.war;

import com.ww.game.play.PlayManager;
import com.ww.game.play.command.PlayPrepareNextTaskCommand;
import com.ww.game.play.command.skill.PlaySkillResetDisguisesCommand;
import com.ww.game.play.command.skill.PlaySkillResetUsedAllCommand;
import com.ww.game.play.command.skill.PlaySkillUnblockAllCommand;
import com.ww.game.play.state.PlayRandomTaskPropsState;
import com.ww.game.play.state.PlayState;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayModelFiller.fillModelTaskMeta;
import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelActiveMemberAddOns;
import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelSkills;

public class PlayWarRandomTaskPropsState extends PlayRandomTaskPropsState {
    public PlayWarRandomTaskPropsState(PlayManager manager) {
        super(manager);
    }

    @Override
    public void initCommands() {
        super.initCommands();
        for (RivalTeam team : getContainer().getTeams().getTeams()) {
            commands.add(new PlaySkillResetUsedAllCommand((WarTeam) team));
            commands.add(new PlaySkillResetDisguisesCommand((WarTeam) team));
        }
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelSkills(model, (WarTeam) team, (WarTeam) opponentTeam);
        fillModelActiveMemberAddOns(model, (WarTeam) team, (WarTeam) opponentTeam);
        return model;
    }

    @Override
    public void after() {
        manager.getFlow().run("CHOOSING_WHO_ANSWER");
    }
}
