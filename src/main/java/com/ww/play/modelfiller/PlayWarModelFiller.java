package com.ww.play.modelfiller;

import com.ww.model.container.rival.war.WarTeam;

import java.util.Map;

import static com.ww.helper.TeamHelper.mapToTeamDTOs;

public class PlayWarModelFiller {
    public static void fillModelPresentIndexes(Map<String, Object> model, WarTeam warTeam, WarTeam warOpponentTeam) {
        model.put("presentIndexes", warTeam.getPresentIndexes());
        model.put("opponentPresentIndexes", warOpponentTeam.getPresentIndexes());
    }

    public static void fillModelSkills(Map<String, Object> model, WarTeam warTeam, WarTeam warOpponentTeam) {
        model.put("skills", warTeam.getTeamSkills().getSkills());
        model.put("opponentSkills", warOpponentTeam.getTeamSkills().getSkills());
    }

    public static void fillModelTeams(Map<String, Object> model, WarTeam warTeam, WarTeam warOpponentTeam) {
        model.put("team", mapToTeamDTOs(warTeam.getTeamMembers()));
        model.put("opponentTeam", mapToTeamDTOs(warOpponentTeam.getTeamMembers()));
    }
}
