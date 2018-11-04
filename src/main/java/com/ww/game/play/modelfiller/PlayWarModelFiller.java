package com.ww.game.play.modelfiller;

import com.ww.game.GameState;
import com.ww.game.member.state.wisie.MemberWisieState;
import com.ww.model.constant.wisie.MemberWisieStatus;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WisieTeamMember;
import com.ww.model.dto.rival.ActiveTeamMemberDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public static void fillModelActiveIndexes(Map<String, Object> model, WarTeam warTeam, WarTeam warOpponentTeam) {
        model.put("activeIndex", warTeam.getActiveIndex());
        model.put("opponentActiveIndex", warOpponentTeam.getActiveIndex());
    }

    public static void fillModelIsChosenActiveIndex(Map<String, Object> model, WarTeam warTeam) {
        model.put("isChosenActiveIndex", warTeam.isChosenActiveIndex());
    }

    public static void fillModelActiveMemberAddOns(Map<String, Object> model, WarTeam warTeam, WarTeam warOpponentTeam) {
        model.put("activeMemberAddOn", new ActiveTeamMemberDTO(warTeam.getActiveTeamMember()));
        model.put("opponentActiveMemberAddOn", new ActiveTeamMemberDTO(warOpponentTeam.getActiveTeamMember()));
    }

    public static void fillModelWisieActions(Map<String, Object> model, WarTeam warTeam, WarTeam warOpponentTeam) {
        if (!warTeam.getActiveTeamMember().isWisie()) {
            return;
        }
        model.put("wisieActions", prepareWisieActions((WisieTeamMember) warTeam.getActiveTeamMember()));
        if (!warOpponentTeam.getActiveTeamMember().isWisie()) {
            return;
        }
        model.put("opponentWisieActions", prepareWisieActions((WisieTeamMember) warOpponentTeam.getActiveTeamMember()));
    }

    private static List<String> prepareWisieActions(WisieTeamMember wisieTeamMember) {
        List<MemberWisieStatus> actions = wisieTeamMember.currentManager().getContainer().getActions();
        return actions.subList(Math.max(0, actions.size() - 2), actions.size()).stream()
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public static void fillModelNullWisieActions(Map<String, Object> model) {
        model.put("wisieActions", null);
        model.put("opponentWisieActions", null);
    }
}
