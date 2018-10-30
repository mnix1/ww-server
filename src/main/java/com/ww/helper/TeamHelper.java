package com.ww.helper;

import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.container.rival.war.WarWisie;
import com.ww.model.container.rival.war.WisieTeamMember;
import com.ww.model.container.rival.war.WisorTeamMember;
import com.ww.model.dto.rival.TeamMemberDTO;
import com.ww.model.dto.social.ExtendedProfileDTO;
import com.ww.model.dto.wisie.WarProfileWisieDTO;
import com.ww.model.entity.outside.rival.campaign.ProfileCampaign;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.OwnedWisie;
import com.ww.model.entity.outside.wisie.ProfileCampaignWisie;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TeamHelper {
    public static final Long BOT_PROFILE_ID = -1L;

    public static boolean isBotProfile(Profile profile) {
        return profile.getId().equals(BOT_PROFILE_ID);
    }

    public static List<TeamMember> prepareTeamMembers(Profile profile, List<? extends OwnedWisie> wisies) {
        List<TeamMember> teamMembers = new ArrayList<>();
        int index = 0;
        teamMembers.add(new WisorTeamMember(index++, profile, new ExtendedProfileDTO(profile)));
        for (OwnedWisie wisie : wisies) {
            WarWisie warWisie = new WarWisie(wisie);
            teamMembers.add(new WisieTeamMember(index++, warWisie, new WarProfileWisieDTO(warWisie)));
        }
        return teamMembers;
    }

    public static List<TeamMember> prepareTeamMembers(List<? extends OwnedWisie> wisies) {
        List<TeamMember> teamMembers = new ArrayList<>();
        int index = 0;
        for (OwnedWisie wisie : wisies) {
            WarWisie warWisie = new WarWisie(wisie);
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

    public static List<TeamMemberDTO> mapToTeamDTOs(List<TeamMember> teamMembers) {
        return teamMembers.stream().map(TeamMemberDTO::new).collect(Collectors.toList());
    }
}
