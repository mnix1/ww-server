package com.ww.game.play.state.skill;

import com.ww.game.GameState;
import com.ww.game.play.PlayManager;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.RivalTeams;
import com.ww.model.container.rival.war.WarTeam;

import java.util.HashMap;
import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelSkills;

public class PlaySkillActionState extends GameState {
    protected PlayManager manager;

    public PlaySkillActionState(PlayManager manager) {
        this.manager = manager;
    }

    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = new HashMap<>();
        fillModelSkills(model, (WarTeam) team, (WarTeam) opponentTeam);
        return model;
    }

    @Override
    public void updateNotify() {
        RivalTeams teams = manager.getContainer().getTeams();
        teams.forEachTeam(team -> {
            manager.getCommunication().sendAndUpdateModel(team.getProfileId(), this.prepareModel(team, teams.opponent(team)));
        });
    }
}
