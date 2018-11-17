package com.ww.game.play.state.war;

import com.ww.game.play.PlayManager;
import com.ww.game.play.command.war.PlayWarInitPassiveSkillsCommand;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.game.play.container.PlayContainer;
import com.ww.game.play.state.PlayIntroState;
import com.ww.model.container.rival.war.skill.PassiveSkillsInit;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.*;

public class PlayWarIntroState extends PlayIntroState {
    public PlayWarIntroState(PlayManager manager) {
        super(manager);
    }

    @Override
    public void initCommands() {
        commands.add(new PlayWarInitPassiveSkillsCommand(getContainer()));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        WarTeam warTeam = (WarTeam) team;
        WarTeam warOpponentTeam = (WarTeam) opponentTeam;
        fillModelPresentIndexes(model, warTeam, warOpponentTeam);
        fillModelSkills(model, warTeam, warOpponentTeam);
        fillModelTeams(model, warTeam, warOpponentTeam);
        return model;
    }
}
