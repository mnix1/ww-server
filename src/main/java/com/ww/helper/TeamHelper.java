package com.ww.helper;

import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.container.rival.RivalModel;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.container.rival.war.WarWisie;
import com.ww.model.container.rival.war.WisieTeamMember;
import com.ww.model.container.rival.war.WisorTeamMember;
import com.ww.model.dto.social.ProfileDTO;
import com.ww.model.dto.social.RivalProfileDTO;
import com.ww.model.dto.wisie.WarProfileWisieDTO;
import com.ww.model.entity.outside.rival.campaign.ProfileCampaign;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.OwnedWisie;
import com.ww.model.entity.outside.wisie.ProfileCampaignWisie;

import java.util.ArrayList;
import java.util.List;

public class TeamHelper {
    public static List<TeamMember> prepareTeamMembers(Profile profile, List<? extends OwnedWisie> wisies, RivalModel model) {
        List<TeamMember> teamMembers = new ArrayList<>();
        int index = 0;
        teamMembers.add(new WisorTeamMember(index++, profile, model.getImportance() == RivalImportance.RANKING ? new RivalProfileDTO(profile, model.getType()) : new ProfileDTO(profile)));
        for (OwnedWisie wisie : wisies) {
            WarWisie warWisie = new WarWisie(wisie, model.findCurrentQuestion());
            teamMembers.add(new WisieTeamMember(index++, warWisie, new WarProfileWisieDTO(warWisie)));
        }
        return teamMembers;
    }

    public static List<TeamMember> prepareTeamMembers(Profile profile, List<? extends OwnedWisie> wisies, RivalImportance importance, RivalType type) {
        List<TeamMember> teamMembers = new ArrayList<>();
        int index = 0;
        teamMembers.add(new WisorTeamMember(index++, profile, importance == RivalImportance.RANKING ? new RivalProfileDTO(profile, type) : new ProfileDTO(profile)));
        for (OwnedWisie wisie : wisies) {
            WarWisie warWisie = new WarWisie(wisie, null);
            teamMembers.add(new WisieTeamMember(index++, warWisie, new WarProfileWisieDTO(warWisie)));
        }
        return teamMembers;
    }

    public static List<TeamMember> prepareTeamMembers(List<? extends OwnedWisie> wisies, RivalModel model) {
        List<TeamMember> teamMembers = new ArrayList<>();
        int index = 0;
        for (OwnedWisie wisie : wisies) {
            WarWisie warWisie = new WarWisie(wisie, model.findCurrentQuestion());
            teamMembers.add(new WisieTeamMember(index++, warWisie, new WarProfileWisieDTO(warWisie)));
        }
        return teamMembers;
    }

    public static List<Integer> preparePresentIndexes(ProfileCampaign profileCampaign, List<TeamMember> teamMembers) {
        List<Integer> presentIndexes = new ArrayList<>();
        if (profileCampaign.getPresent()) {
            presentIndexes.add(0);
        }
        for (ProfileCampaignWisie wisie : profileCampaign.getWisies()) {
            if (!wisie.getDisabled()) {
                int index = teamMembers.stream().filter(teamMember -> teamMember.isWisie() && wisie.equals(((WarWisie) teamMember.getContent()).getWisie())).findFirst().get().getIndex();
                presentIndexes.add(index);
            }
        }
        return presentIndexes;
    }
}
