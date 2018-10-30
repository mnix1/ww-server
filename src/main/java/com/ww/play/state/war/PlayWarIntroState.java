package com.ww.play.state.war;

import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.play.container.PlayContainer;
import com.ww.play.state.PlayIntroState;

import java.util.Map;

import static com.ww.helper.TeamHelper.mapToTeamDTOs;

public class PlayWarIntroState extends PlayIntroState {
    public PlayWarIntroState(PlayContainer container) {
        super(container);
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        WarTeam warTeam = (WarTeam) team;
        WarTeam warOpponentTeam = (WarTeam) opponentTeam;

        model.put("presentIndexes", warTeam.getPresentIndexes());
        model.put("opponentPresentIndexes", warOpponentTeam.getPresentIndexes());

        model.put("skills", warTeam.getTeamSkills().getSkills());
        model.put("opponentSkills", warOpponentTeam.getTeamSkills().getSkills());

        model.put("team", mapToTeamDTOs(warTeam.getTeamMembers()));
        model.put("opponentTeam", mapToTeamDTOs(warOpponentTeam.getTeamMembers()));
        return model;
    }
}
