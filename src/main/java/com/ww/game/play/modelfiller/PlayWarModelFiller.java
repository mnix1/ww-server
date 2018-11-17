package com.ww.game.play.modelfiller;

import com.ww.model.constant.wisie.MemberWisieStatus;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WisieTeamMember;
import com.ww.model.dto.rival.ActiveTeamMemberDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ww.helper.TeamHelper.mapToTeamDTOs;

public class PlayWarModelFiller {
    public static void fillModelPresentIndexes(Map<String, Object> model, WarTeam warTeam, WarTeam warOpponentTeam) {
        model.put("presentIndexes", new ArrayList<>(warTeam.getPresentIndexes()));
        model.put("opponentPresentIndexes", new ArrayList<>(warOpponentTeam.getPresentIndexes()));
    }

    public static void fillModelSkills(Map<String, Object> model, WarTeam warTeam, WarTeam warOpponentTeam) {
        fillModelSkills(model, null, warTeam, warOpponentTeam);
    }

    public static void fillModelSkills(Map<String, Object> model, WarTeam changedTeam, WarTeam warTeam, WarTeam warOpponentTeam) {
        if (changedTeam == null) {
            fillModelSkill(model, "skills", warTeam);
            fillModelSkill(model, "opponentSkills", warOpponentTeam);
        } else if (changedTeam.getProfileId().equals(warTeam.getProfileId())) {
            fillModelSkill(model, "skills", warTeam);
        } else {
            fillModelSkill(model, "opponentSkills", warOpponentTeam);
        }
    }

    private static void fillModelSkill(Map<String, Object> model, String key, WarTeam warTeam) {
        model.put(key, warTeam.getTeamSkills().getSkills());
    }

    public static void fillModelTeams(Map<String, Object> model, WarTeam warTeam, WarTeam warOpponentTeam) {
        fillModelTeams(model, null, warTeam, warOpponentTeam);
    }

    public static void fillModelTeams(Map<String, Object> model, WarTeam changedTeam, WarTeam warTeam, WarTeam warOpponentTeam) {
        if (changedTeam == null) {
            fillModelTeam(model, "team", warTeam);
            fillModelTeam(model, "opponentTeam", warOpponentTeam);
        } else if (changedTeam.getProfileId().equals(warTeam.getProfileId())) {
            fillModelTeam(model, "team", warTeam);
        } else {
            fillModelTeam(model, "opponentTeam", warOpponentTeam);
        }
    }

    private static void fillModelTeam(Map<String, Object> model, String key, WarTeam warTeam) {
        model.put(key, mapToTeamDTOs(warTeam.getTeamMembers()));
    }

    public static void fillModelActiveIndexes(Map<String, Object> model, WarTeam warTeam, WarTeam warOpponentTeam) {
        model.put("activeIndex", warTeam.getActiveIndex());
        model.put("opponentActiveIndex", warOpponentTeam.getActiveIndex());
    }

    public static void fillModelActiveIndex(Map<String, Object> model, WarTeam warTeam) {
        model.put("activeIndex", warTeam.getActiveIndex());
    }

    public static void fillModelIsChosenActiveIndex(Map<String, Object> model, WarTeam warTeam) {
        model.put("isChosenActiveIndex", warTeam.isChosenActiveIndex());
    }

    public static void fillModelActiveMemberAddOns(Map<String, Object> model, WarTeam warTeam, WarTeam warOpponentTeam) {
        model.put("activeMemberAddOn", new ActiveTeamMemberDTO(warTeam.getActiveTeamMember()));
        model.put("opponentActiveMemberAddOn", new ActiveTeamMemberDTO(warOpponentTeam.getActiveTeamMember()));
    }

    public static void fillModelActiveMemberAddOns(Map<String, Object> model, WarTeam changedTeam, WarTeam warTeam, WarTeam warOpponentTeam) {
        if (changedTeam == null) {
            fillModelActiveMemberAddOn(model, "activeMemberAddOn", warTeam);
            fillModelActiveMemberAddOn(model, "opponentActiveMemberAddOn", warOpponentTeam);
        } else if (changedTeam.getProfileId().equals(warTeam.getProfileId())) {
            fillModelActiveMemberAddOn(model, "activeMemberAddOn", warTeam);
        } else {
            fillModelActiveMemberAddOn(model, "opponentActiveMemberAddOn", warOpponentTeam);
        }
    }

    private static void fillModelActiveMemberAddOn(Map<String, Object> model, String key, WarTeam warTeam) {
        model.put(key, new ActiveTeamMemberDTO(warTeam.getActiveTeamMember()));
    }

    public static void fillModelWisieActions(Map<String, Object> model, WarTeam warTeam, WarTeam warOpponentTeam) {
        fillModelWisieActions(model, null, warTeam, warOpponentTeam);
    }

    public static void fillModelWisieActions(Map<String, Object> model, WarTeam changedTeam, WarTeam warTeam, WarTeam warOpponentTeam) {
        if (!warTeam.getActiveTeamMember().isWisie()) {
            return;
        }
        if (changedTeam == null) {
            model.put("wisieActions", prepareWisieActions((WisieTeamMember) warTeam.getActiveTeamMember()));
            if (!warOpponentTeam.getActiveTeamMember().isWisie()) {
                return;
            }
            model.put("opponentWisieActions", prepareWisieActions((WisieTeamMember) warOpponentTeam.getActiveTeamMember()));
        } else if (changedTeam.getProfileId().equals(warTeam.getProfileId())) {
            model.put("wisieActions", prepareWisieActions((WisieTeamMember) warTeam.getActiveTeamMember()));
        } else {
            if (!warOpponentTeam.getActiveTeamMember().isWisie()) {
                return;
            }
            model.put("opponentWisieActions", prepareWisieActions((WisieTeamMember) warOpponentTeam.getActiveTeamMember()));
        }
    }

    private static List<String> prepareWisieActions(WisieTeamMember wisieTeamMember) {
        List<MemberWisieStatus> actions = wisieTeamMember.currentManager().get().getContainer().getActions();
        return actions.subList(Math.max(0, actions.size() - 2), actions.size()).stream()
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
